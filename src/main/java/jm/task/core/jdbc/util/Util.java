package jm.task.core.jdbc.util;

import lombok.SneakyThrows;
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
    private static Properties jdbcMysql = new Properties();
    private static String driver;
    private static String url;
    private static String dbName;
    private static String userName;
    private static String pwd;

    private static Connection BdConnection;

    static {
        try (InputStream in = Files.newInputStream(PATH_TO_CONFIG)) {
            jdbcMysql.load(in);
            driver = jdbcMysql.getProperty("driver",
                    "com.mysql.cj.jdbc.Driver");
            url = jdbcMysql.getProperty("url",
                    "jdbc:mysql://localhost:3306/");
            dbName = jdbcMysql.getProperty("dbName", "@myLocalBD");
            userName = jdbcMysql.getProperty("userName", "admin");
            pwd = jdbcMysql.getProperty("pwd", "admin");
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
        connect();
        return getBdConnection();
    }

    private static void connect() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        try {
            BdConnection = DriverManager.getConnection(
                    url + dbName, userName, pwd);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}