package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {
// TODO:Обработка всех исключений, связанных с работой с базой данных должна находиться в dao
//  Создание таблицы для User(ов) – не должно приводить к исключению, если такая таблица уже существует
//  Удаление таблицы User(ов) – не должно приводить к исключению, если таблицы не существует
//  Очистка содержания таблицы
//  Добавление User в таблицу
//  Удаление User из таблицы ( по id )
//  Получение всех User(ов) из таблицы
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
