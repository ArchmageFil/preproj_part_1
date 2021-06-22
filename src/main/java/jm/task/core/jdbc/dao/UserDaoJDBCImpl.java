package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;

@SuppressWarnings({"SpellCheckingInspection", "SqlResolve"})
public class UserDaoJDBCImpl implements UserDao {
    private final Connection conn;
    private final Logger logger = LogManager.getLogger();

    public UserDaoJDBCImpl() {
        conn = Util.getBdConnection();

// TODO:Обработка всех исключений, связанных с работой с базой данных должна находиться в dao
//  Создание таблицы для User(ов) – не должно приводить к исключению, если такая таблица уже существует
//  Удаление таблицы User(ов) – не должно приводить к исключению, если таблицы не существует
//  Очистка содержания таблицы
//  Добавление User в таблицу
//  Удаление User из таблицы ( по id )
//  Получение всех User(ов) из таблицы
    }

    public void createUsersTable() {
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE `mydbtest`.`lesson_1.1.3` (\n" +
                    "  `id` BIGINT(64) NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` VARCHAR(255) NOT NULL,\n" +
                    "  `lastName` VARCHAR(255) NOT NULL,\n" +
                    "  `age` TINYINT(8) NOT NULL,\n" +
                    "  PRIMARY KEY (`id`))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8;";
            stmt.execute(query);
            logger.info("БД Создана");
        } catch (SQLException sqlException) {
            logger.warn("Вышибло при создании");
            logger.warn(sqlException);
        }
    }

    public void dropUsersTable() {
        try {
            Statement stmt = conn.createStatement();
            String sql = "DROP TABLE `mydbtest`.`lesson_1.1.3`";
            stmt.executeUpdate(sql);
            logger.info("БД удалена");
        } catch (SQLException sqlException) {
            logger.warn("Вышибло при удалении");
            logger.warn(sqlException);
        }
    }
    // TODO После каждого добавления должен быть вывод в консоль ( User с именем – name добавлен в базу данных )
    public void saveUser(String name, String lastName, byte age) {
        try {
            Statement stmt = conn.createStatement();
            String query = "";                          // TODO: реализовать
            stmt.execute(query);
            logger.info("Пользователь " + name + "  Создан");
        } catch (SQLException sqlException) {
            logger.warn("Вышибло при создании");
            logger.warn(sqlException);
        }
    }

    public void removeUserById(long id) {
        try {
            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM `mydbtest`.`lesson_1.1.3` \" +\n" +
                    "            \"WHERE id = " + id;
            stmt.executeUpdate(sql);
            logger.info("Посльзователь {} cтерт, наверное", id);
        } catch (SQLException sqlException) {
            logger.warn("Вышибло при стирании пользователя по АйПи");
            logger.warn(sqlException);
        }
    }

    public List<User> getAllUsers() {
        // TODO: реализовать
        return null;
    }

    public void cleanUsersTable() {
        try {
            Statement stmt = conn.createStatement();
            String sql = "";
            stmt.executeUpdate(sql);
            logger.info("Таблица потерта");
        } catch (SQLException sqlException) {
            logger.warn("Вышибло при стирании всех данных в БД");
            logger.warn(sqlException);
        }
    }
}

