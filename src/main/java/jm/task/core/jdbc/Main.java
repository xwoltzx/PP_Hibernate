package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // реализуйте алгоритм здесь
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Vitaliy", "Yudin", (byte) 21);
        userService.saveUser("Andrey", "Gogunskiy", (byte) 45);
        userService.saveUser("Alexey", "Bianki", (byte) 65);
        userService.saveUser("Vyacheslav", "Klichko", (byte) 52);
        List<User> userList = userService.getAllUsers();
        for (User user : userList) {
            System.out.println(user);
        }
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
