package notebook.controller;

import notebook.model.User;
import notebook.model.repository.GBRepository;

import java.util.List;
import java.util.Objects;

public class UserController {
    private final GBRepository repository;

    public UserController(GBRepository repository) {
        this.repository = repository;
    }

    public void saveUser(User user) {
        repository.create(user);
    }

    public User readUser(Long userId) throws Exception {
        List<User> users = repository.findAll();
        for (User user : users) {
            if (Objects.equals(user.getId(), userId)) {
                return user;
            }
        }

        throw new RuntimeException("User not found");
    }

    public void updateUser(String userId, User update) {
        update.setId(Long.parseLong(userId));
        repository.update(Long.parseLong(userId), update);
    }

    public List<User> findAll(){
        return repository.findAll();
    }

    public User getNewUser(String firstName, String lastName, String phone) {
        return repository.getNewUser(firstName, lastName, phone);
    }

    public boolean deleteUser(String Id) {
        Long userId = Long.parseLong(Id);
        return repository.delete(userId);
    }

    public void addAll(List<User> addedUsers) {
        repository.addAll(addedUsers);
    }
}
