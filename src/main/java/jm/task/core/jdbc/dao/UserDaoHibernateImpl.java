package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {
        // пока не трогаем
    }


    @Override
    public void createUsersTable() {
        // пока не трогаем
    }

    @Override
    public void dropUsersTable() {
        // пока не трогаем
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        // пока не трогаем
    }

    @Override
    public void removeUserById(long id) {
        // пока не трогаем
    }

    @Override
    public List<User> getAllUsers() {
        // пока не трогаем
        return null;
    }

    @Override
    public void cleanUsersTable() {
        // пока не трогаем
    }
}
