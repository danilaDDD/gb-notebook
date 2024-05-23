package notebook.view;

import notebook.controller.UserController;
import notebook.model.User;
import notebook.util.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserView {
    private final UserController userController;

    public UserView(UserController userController) {
        this.userController = userController;
    }

    public void run(){
        Commands com;

        while (true) {
            String command = prompt("Введите команду: ");
            com = Commands.valueOf(command);
            if (com == Commands.EXIT) return;
            switch (com) {
                case HELP:
                    for(Commands c: Commands.values()){
                        System.out.println(c);
                    }
                    break;
                case ADD_ALL:
                    addAllUsersCommand();
                    break;

                case CREATE:
                    User u = userPrompt();
                    userController.saveUser(u);
                    break;
                case READ:
                    String id = userIdPrompt();
                    try {
                        User user = userController.readUser(Long.parseLong(id));
                        System.out.println(user);
                        System.out.println();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case UPDATE:
                    userController.updateUser(userIdPrompt(), userPrompt());
                    printAllUsers();
                    break;

                case LIST:
                    printAllUsers();
                    break;

                case DELETE:
                    System.out.println(userController.deleteUser(userIdPrompt()));
                    printAllUsers();
                    break;
            }
        }
    }

    private void addAllUsersCommand(){
        String commandFlag = addAllPrompt();
        List<User> addedUsers = new ArrayList<>();

        while(commandFlag.equals("add")){
            User user = userPrompt();
            addedUsers.add(user);
            commandFlag = addAllPrompt();
        }

        userController.addAll(addedUsers);
        System.out.println("all users added");
    }

    private String addAllPrompt(){
        return prompt("Введите add для создания пользователя или любое другое слово для прекращения команды: ");
    }

    private String prompt(String message) {
        Scanner in = new Scanner(System.in);
        System.out.print(message);
        return in.nextLine();
    }

    private void printAllUsers(){
        System.out.println(userController.findAll());
    }

    private String userIdPrompt(){
        return prompt("Идентификатор пользователя: ");
    }

    private User userPrompt() {
        String firstName = prompt("Имя: ");
        String lastName = prompt("Фамилия: ");
        String phone = prompt("Номер телефона: ");
        return userController.getNewUser(firstName, lastName, phone);
    }
}
