package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getHibernateConnection;

@SuppressWarnings("SqlResolve")
public class UserDaoHibernateImpl implements UserDao {
    private static final Logger LOGGER = LogManager.getLogger();

    public UserDaoHibernateImpl() {
    }


    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public void createUsersTable() {
        try {
            Session session = getHibernateConnection();
            session.getTransaction().begin();
            int n = session.createSQLQuery(
                    "CREATE TABLE `mydbtest`.`lesson_114` (\n" +
                            "  `id` BIGINT(64) NOT NULL AUTO_INCREMENT,\n" +
                            "  `name` VARCHAR(255) NOT NULL,\n" +
                            "  `lastName` VARCHAR(255) NOT NULL,\n" +
                            "  `age` TINYINT(8) NOT NULL,\n" +
                            "  PRIMARY KEY (`id`))\n" +
                            "ENGINE = InnoDB\n" +
                            "DEFAULT CHARACTER SET = utf8;").executeUpdate();
            LOGGER.info("БД Создана, операций: {}", n);
            session.close();
        } catch (Exception e) {
            LOGGER.warn("Вышибло при создании");
            LOGGER.warn(e);
        }
    }

    @Override
    public void dropUsersTable() {
        try {
            Session session = getHibernateConnection();
            session.getTransaction().begin();
            int n = session.createSQLQuery(
                    "DROP TABLE `mydbtest`.`lesson_114`").executeUpdate();
            LOGGER.info("БД удалена, операций: {}", n);
            session.close();
        } catch (Exception e) {
            LOGGER.warn("Вышибло при удалении");
            LOGGER.warn(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            User user = new User(name, lastName, age);
            Session session = getHibernateConnection();
            session.getTransaction().begin();
            session.persist(user);
            session.getTransaction().commit();
            session.close();
            System.out.printf("User с именем – %s добавлен в базу данных \n", name);
            LOGGER.info("Пользователь " + name + ": cоздан");
        } catch (Exception e) {
            LOGGER.warn("Вышибло при создании");
            LOGGER.warn(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            Session session = getHibernateConnection();
            session.getTransaction().begin();
            Query query = session.createQuery("delete from User u where u.id = :userId");
            query.setParameter("userId", id);
            int n = query.executeUpdate();
            LOGGER.info("Пользователь {} удален, удалений: {}", id, n);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            LOGGER.warn("Вышибло при стирании пользователя по АйПи");
            LOGGER.warn(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getAllUsers() {
        try {
            Session session = getHibernateConnection();
            session.getTransaction().begin();
            Query query = session.createQuery("select u from User u");
            List<User> users = query.getResultList();
            LOGGER.info("Создан список пользователей, в количестве: {}",
                    users.size());
            session.close();
            return users;
        } catch (Exception e) {
            LOGGER.warn("Вышибло при считывании всех пользователей БД");
            LOGGER.warn(e);
        }
        return Collections.emptyList();
    }

    @Override
    public void cleanUsersTable() {
        try {
            Session session = getHibernateConnection();
            session.getTransaction().begin();
            Query query = session.createQuery("delete from User u");
            int n = query.executeUpdate();
            LOGGER.info("Пользователей далено: {}", n);
            session.close();
        } catch (Exception e) {
            LOGGER.warn("Вышибло при стирании всех данных в БД");
            LOGGER.warn(e);
        }
    }
}
