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
        conn = Util.getJdbcConnection();
    }

    public void createUsersTable() {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE `mydbtest`.`lesson_1.1.3` (\n" +
                    "  `id` BIGINT(64) NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` VARCHAR(255) NOT NULL,\n" +
                    "  `lastName` VARCHAR(255) NOT NULL,\n" +
                    "  `age` TINYINT(8) NOT NULL,\n" +
                    "  PRIMARY KEY (`id`))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8;");
            logger.info("БД Создана");
        } catch (SQLException sqlException) {
            logger.warn("Вышибло при создании");
            logger.warn(sqlException);
        }
    }

    public void dropUsersTable() {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DROP TABLE `mydbtest`.`lesson_1.1.3`");
            logger.info("БД удалена");
        } catch (SQLException sqlException) {
            logger.warn("Вышибло при удалении");
            logger.warn(sqlException);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "insert into mydbtest.`lesson_1.1.3` (name, lastname, age) "
                        + "Values (?, ?, ?)")) {
            conn.setAutoCommit(false);
            stmt.setString(1, name);
            stmt.setString(2, lastName);
            stmt.setByte(3, age);
            stmt.addBatch();
            stmt.executeUpdate();

            conn.commit();
            System.out.printf("User с именем – %s добавлен в базу данных \n", name);
            logger.info("Пользователь " + name + ": cоздан");
            conn.setAutoCommit(true);
        } catch (SQLException sqlException) {
            logger.warn("Вышибло при создании");
            logger.warn(sqlException);
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                logger.warn("Вышибло при откате транзакции");
            }
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM `lesson_1.1.3` where id = ?")) {
            conn.setAutoCommit(false);
            stmt.setLong(1, id);
            stmt.addBatch();
            stmt.executeUpdate();
            conn.commit();
            logger.info("Посльзователь {} cтерт, наверное", id);
            conn.setAutoCommit(true);
        } catch (SQLException sqlException) {
            logger.warn("Вышибло при стирании пользователя по АйПи");
            logger.warn(sqlException);
        }
    }

    public List<User> getAllUsers() {
        List<User> listUser = new LinkedList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM mydbtest.`lesson_1.1.3`;");
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
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("TRUNCATE table mydbtest.`lesson_1.1.3`");
            logger.info("Таблица потерта");
        } catch (SQLException sqlException) {
            logger.warn("Вышибло при стирании всех данных в БД");
            logger.warn(sqlException);
        }
    }
}

