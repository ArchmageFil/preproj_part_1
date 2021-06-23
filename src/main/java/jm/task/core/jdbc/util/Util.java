package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private final static Path PATH_TO_CONFIG = Path.of(
            "src/main/resources/jdbc-mysql.properties");
    private static final Properties JDBC_MYSQL = new Properties();
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int CONNECTION_TIMEOUT = 2;
    // параметры для драйвера
    private static String driver;
    private static String url;
    private static String dbName;
    private static String userName;
    private static String pwd;
    //параметры для Hibernate
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "validate";
    private static final String dialect = "org.hibernate.dialect.MySQLDialect";
    private static SessionFactory sessionFactory;

    private static volatile boolean run = false;
    private static volatile Connection bdConnection;

    //  Статический блок для подгрузки настроек
    static {
        try (InputStream in = Files.newInputStream(PATH_TO_CONFIG)) {
            JDBC_MYSQL.load(in);
            driver = JDBC_MYSQL.getProperty("driver",
                    "com.mysql.cj.jdbc.Driver");
            url = JDBC_MYSQL.getProperty("url",
                    "jdbc:mysql://localhost:3306/");
            dbName = JDBC_MYSQL.getProperty("dbName", "@myLocalBD");
            userName = JDBC_MYSQL.getProperty("userName", "admin");
            pwd = JDBC_MYSQL.getProperty("pwd", "admin");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Проверяет, был ли уже запущен наблюддатель, и если нет - создает.
     * Затем возвдащает созданное или наличествующее  соединение к БД.
     *
     * @return Рабочее соединение с БД
     */
    public static Connection getJdbcConnection() {
        if (!run) {
            Thread connectionDaemon = new Thread(getDaemon());
            connectionDaemon.setDaemon(run = true);
            connectionDaemon.start();
        }
        while (bdConnection == null) {
            Thread.onSpinWait();
        }
        return bdConnection;
    }

    /**
     * @return Новую сессию от Фабрики сессий
     */
    public static Session getHibernateConnection() {
        if (sessionFactory == null) {
            sessionFactory = createSessionFactory(mySqlConfig());
            LOGGER.info("создали фабрику сессий");
        }
        return sessionFactory.openSession();
    }

    /**
     * Загрузка драйвера JDBC и создание подключения
     */
    private static void connect() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException cnfe) {
            LOGGER.warn("Класс драйвера не загрузился");
            LOGGER.warn(cnfe);
        }
        try {
            bdConnection = DriverManager.getConnection(
                    url + dbName, userName, pwd);
        } catch (SQLException sqlException) {
            LOGGER.warn("Соединение обломалось. Адрес:{} БД:{} Логин:{} Пароль:{}",
                    url, dbName, userName, pwd);
            LOGGER.warn(sqlException);
        }
    }

    /**
     * Внутренний метод для созданиия фбрики сессий
     *
     * @param config - org.hibernate.cfg.Configuration;
     * @return готовую Фабрику Сессий
     */
    private static SessionFactory createSessionFactory(Configuration config) {
        LOGGER.info("Создаем Фабрику сессий с конфигом {}",
                config.getProperties().toString());
        StandardServiceRegistryBuilder sSRB = new StandardServiceRegistryBuilder();
        sSRB.applySettings(config.getProperties());
        ServiceRegistry sr = sSRB.build();
        return config.buildSessionFactory(sr);
    }

    /**
     * Шаблон для наблюдателя за соединением к БД. Сам себя назначить не может
     * - вылетает исключение
     *
     * @return Демон, следящий за соединением.
     */
    @SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
    private static Runnable getDaemon() {
        return () -> {
            LOGGER.info("Запустили демона run = {}", run);
            while (true) {
                if (bdConnection == null) {
                    LOGGER.info("Соединение потеряно, перезапускаем");
                    connect();
                } else {
                    try {
                        if (bdConnection.isValid(CONNECTION_TIMEOUT)) {
                            Thread.sleep(3000);
                        }
                    } catch (SQLException | InterruptedException sqlIntEx) {
                        LOGGER.warn("При проверке соединения с БД случилась фигня");
                        LOGGER.warn(sqlIntEx);
                    }
                }
            }
        };
    }

    /**
     * Создает конфиг Hibernate для mySQL
     *
     * @return загрузчик для Фабрики сессий
     */
    private static Configuration mySqlConfig() {
        Configuration config = new Configuration();
        config.addAnnotatedClass(User.class);
        config.setProperty("hibernate.dialect", dialect);
        config.setProperty("hibernate.connection.driver_class", driver);
        config.setProperty("hibernate.connection.url", url + dbName);
        config.setProperty("hibernate.connection.username", userName);
        config.setProperty("hibernate.connection.password", pwd);
        config.setProperty("hibernate.show_sql", hibernate_show_sql);
        config.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return config;
    }
}