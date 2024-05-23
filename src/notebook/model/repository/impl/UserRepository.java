package notebook.model.repository.impl;

import notebook.model.repository.DBRepository;
import notebook.util.UserValidator;
import notebook.util.mapper.impl.UserMapper;
import notebook.model.User;
import notebook.model.repository.GBRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements GBRepository {
    private final UserMapper mapper;
    private final DBRepository operation;
    private final UserValidator userValidator = new UserValidator();

    public UserRepository(DBRepository operation) {
        this.mapper = new UserMapper();
        this.operation = operation;
    }

    @Override
    public List<User> findAll() {
        List<String> lines = operation.readAll();
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            users.add(mapper.toOutput(line));
        }
        return users;
    }

    @Override
    public User create(User user) {
        userValid(user);
        List<User> users = findAll();
        long max = getMaxUserId();
        long next = max + 1;
        user.setId(next);
        users.add(user);
        write(users);
        return user;
    }

    private long getMaxUserId(){
        List<User> users = findAll();
        long max = 0L;
        for (User u : users) {
            long id = u.getId();
            if (max < id){
                max = id;
            }
        }

        return max;
    }

    private void userValid(User user){
        user.setFirstName(userValidator.validateName(user.getFirstName()));
        user.setLastName(userValidator.validateName(user.getLastName()));
    }

    @Override
    public Optional<User> update(Long userId, User update) {
        List<User> users = findAll();
        User editUser = findUserByIdOrThrow(userId, users);

        if(!update.getFirstName().isEmpty())
            editUser.setFirstName(update.getFirstName());
        if(!update.getLastName().isEmpty())
            editUser.setLastName(update.getLastName());
        if(!update.getPhone().isEmpty())
            editUser.setPhone(update.getPhone());

        write(users);

        return Optional.of(update);
    }

    @Override
    public boolean delete(Long id) {
        List<User> users = findAll();
        User deletedUser = findUserByIdOrThrow(id, users);
        users.remove(deletedUser);
        write(users);
        return true;
    }

    @Override
    public User getNewUser(String firstName, String lastName, String phone) {
        return new User(firstName, lastName, phone);
    }

    @Override
    public void addAll(List<User> addedUsers) {
        List<User> users = findAll();
        Long nextId = users
                .stream()
                .mapToLong(User::getId).max()
                .getAsLong() + 1;

        for (int i = 0; i < addedUsers.size(); i++) {
            User u = addedUsers.get(i);
            u.setId(nextId);
            users.add(u);
            nextId++;
        }
        write(users);
    }

    private void write(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User u: users) {
            lines.add(mapper.toInput(u));
        }
        operation.saveAll(lines);
    }

    private User findUserByIdOrThrow(Long userId, List<User> users){
        return users.stream()
                .filter(u -> u.getId()
                        .equals(userId))
                .findFirst().orElseThrow(() -> new RuntimeException("User not found"));
    }

}
