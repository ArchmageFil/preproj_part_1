package jm.task.core.jdbc.util;

import lombok.SneakyThrows;
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
    private static String driver;
    private static String url;
    private static String dbName;
    private static String userName;
    private static String pwd;

    private static Connection BdConnection;

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

    @SneakyThrows
    public static Connection getBdConnection() {
        if (BdConnection != null) {
            if (!BdConnection.isClosed()) {
                return BdConnection;
            }
        }
        LOGGER.info("Соединение потеряно, перезапускаем");
        connect();
        return getBdConnection();
    }

    private static void connect() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException cnfe) {
            LOGGER.warn("Класс драйвера не загрузился");
            LOGGER.warn(cnfe);
        }
        try {
            BdConnection = DriverManager.getConnection(
                    url + dbName, userName, pwd);
        } catch (SQLException sqlException) {
            LOGGER.warn("Соединение обломалось. Адрес:{} БД:{} Логин:{} Пароль:{}",
                    url, dbName, userName, pwd);
            LOGGER.warn(sqlException);
        }
    }
}