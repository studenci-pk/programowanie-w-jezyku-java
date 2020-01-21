package pl.edu.pk.biblioteka.utils;

import org.apache.log4j.Logger;
import java.sql.*;

/**
 * Klasa to łączenia się z baza danych (tworzenia połączenia)
 */
public class DatabaseConnector {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_NAME = "biblioteka";
    private static final String HOST = "localhost";
    private static final String URL = String.format("jdbc:mysql://%s/%s?characterEncoding=UTF-8", HOST, DATABASE_NAME);
    private static final String USER = "biblioteka_admin";
    private static final String PASSWORD = "Ag@tka123";
    private static final Logger logger = Logger.getLogger(DatabaseConnector.class.getName());

    /**
     * Metoda tworząca połączenie z bazą danych z zdefiniownych w niej stałych
     * @return połączenie z bazą danych
     */
    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e);
        }

        return null;
    }
}