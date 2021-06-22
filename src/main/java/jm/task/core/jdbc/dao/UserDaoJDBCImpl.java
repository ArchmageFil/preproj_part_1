package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({"SpellCheckingInspection", "SqlResolve"})
public class UserDaoJDBCImpl implements UserDao {
    private final Connection conn;
    private final Logger logger = LogManager.getLogger();

    public UserDaoJDBCImpl() {
        conn = Util.getBdConnection();
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

    public void saveUser(String name, String lastName, byte age) {
        try {
            Statement stmt = conn.createStatement();
            String query = String.format(
                    "insert into mydbtest.`lesson_1.1.3` (name, lastname, age) Values (\"%s\", \"%s\", %d)",
                    name, lastName, age);
            stmt.execute(query);
            System.out.printf("User с именем – %s добавлен в базу данных \n", name);
            logger.info("Пользователь " + name + ": cоздан");
        } catch (SQLException sqlException) {
            logger.warn("Вышибло при создании");
            logger.warn(sqlException);
        }
    }

    public void removeUserById(long id) {
        try {
            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM `lesson_1.1.3` where id = " + id;
            stmt.executeUpdate(sql);
            logger.info("Посльзователь {} cтерт, наверное", id);
        } catch (SQLException sqlException) {
            logger.warn("Вышибло при стирании пользователя по АйПи");
            logger.warn(sqlException);
        }
    }

    public List<User> getAllUsers() {
        List<User> listUser = new LinkedList<>();
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM mydbtest.`lesson_1.1.3`;";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                listUser.add(new User(rs.getLong(1), rs.getString(2),
                        rs.getString(3), rs.getByte(4)));
            }
            logger.info("Считали в Список {} штук", listUser.size());
        } catch (SQLException sqlException) {
            logger.warn("Вышибло при считывании всех пользователей БД");
            logger.warn(sqlException);
        }
        return listUser;
    }

    public void cleanUsersTable() {
        try {
            Statement stmt = conn.createStatement();
            String sql = "TRUNCATE table mydbtest.`lesson_1.1.3`";
            stmt.executeUpdate(sql);
            logger.info("Таблица потерта");
        } catch (SQLException sqlException) {
            logger.warn("Вышибло при стирании всех данных в БД");
            logger.warn(sqlException);
        }
    }
}

