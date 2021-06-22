package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserService us = new UserServiceImpl();
        us.createUsersTable();

        us.saveUser("Ася", "Казанцева", (byte) 10);
        us.saveUser("Иван", "Иванов", (byte) 20);
        us.saveUser("Сидр", "Сидоров", (byte) 30);
        us.saveUser("Василий", "Васин", (byte) 40);

        us.getAllUsers().forEach(System.out::println);

        us.cleanUsersTable();
        us.dropUsersTable();
    }
}
