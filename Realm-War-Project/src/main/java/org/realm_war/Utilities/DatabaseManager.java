package org.realm_war.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String BASE_URL = "jdbc:postgresql://localhost:5432/";

    public static Connection getConnection(String DBName, String user, String password) throws SQLException {
        String fullURL = BASE_URL + DBName;
        return DriverManager.getConnection(fullURL, user, password);
    }
}
