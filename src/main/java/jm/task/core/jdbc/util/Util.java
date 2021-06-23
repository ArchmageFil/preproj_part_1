package jm.task.core.jdbc.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private static volatile Connection bdConnection;
    private static volatile boolean run = false;

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
    public static Connection getBdConnection() {
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
     * Загрузка драйвера и создание подключения
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
}