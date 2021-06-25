package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getHibernateConnection;

@SuppressWarnings("SqlResolve")
public class UserDaoHibernateImpl implements UserDao {
    private static final Logger LOGGER = LogManager.getLogger();
    private final SessionFactory sFactory;

    public UserDaoHibernateImpl() {
        sFactory = getHibernateConnection();
    }


    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public void createUsersTable() {
        Session session = sFactory.openSession();
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
    }

    @Override
    public void dropUsersTable() {
        Session session = sFactory.openSession();
        session.getTransaction().begin();
        int n = session.createSQLQuery(
                "DROP TABLE `mydbtest`.`lesson_114`").executeUpdate();
        LOGGER.info("БД удалена, операций: {}", n);
        session.close();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        Session session = sFactory.openSession();
        session.getTransaction().begin();
        session.persist(user);
        session.getTransaction().commit();
        session.close();
        System.out.printf("User с именем – %s добавлен в базу данных \n", name);
        LOGGER.info("Пользователь " + name + ": cоздан");
    }

    @Override
    public void removeUserById(long id) {
        Session session = sFactory.openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("delete from User u where u.id = :userId");
        query.setParameter("userId", id);
        int n = query.executeUpdate();
        LOGGER.info("Пользователь {} удален, удалений: {}", id, n);
        session.getTransaction().commit();
        session.close();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getAllUsers() {
        Session session = sFactory.openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("select u from User u");
        List<User> users = query.getResultList();
        LOGGER.info("Создан список пользователей, в количестве: {}",
                users.size());
        session.close();
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = sFactory.openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("delete from User u");
        int n = query.executeUpdate();
        LOGGER.info("Пользователей далено: {}", n);
        session.close();
    }
}
