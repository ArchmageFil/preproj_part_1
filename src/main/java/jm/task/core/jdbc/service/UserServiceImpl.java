package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.model.User;
import java.util.List;

public class UserServiceImpl implements UserService {
    public UserServiceImpl(UserDao dao) {
    }
    public UserServiceImpl() {
    }
    public void createUsersTable() {
        // TODO: реализовать
    }

    public void dropUsersTable() {
        // TODO: реализовать
    }

    public void saveUser(String name, String lastName, byte age) {
        // TODO: реализовать
    }

    public void removeUserById(long id) {
        // TODO: реализовать
    }

    public List<User> getAllUsers() {
        // TODO: реализовать
        return null;
    }

    public void cleanUsersTable() {
        // TODO: реализовать
    }
}
