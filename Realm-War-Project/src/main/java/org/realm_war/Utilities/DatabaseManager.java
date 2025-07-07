package org.realm_war.Utilities;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String BASE_URL = "jdbc:postgresql://localhost:5432/";
    private static final String DEFAULT_DB = "postgres";

    private static void ensureDatabaseExists(String DBName, String user, String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(BASE_URL + DEFAULT_DB, user, password)) {
            String checkDBSQL = "SELECT 1 FROM pg_database WHERE datname = ?";
            boolean DBExists = false;

            try (PreparedStatement ps = conn.prepareStatement(checkDBSQL)) {
                ps.setString(1, DBName.toLowerCase()); // Because postgreSQL handles database names in lowercase.
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        DBExists = true;
                    }
                }
            }

            if (!DBExists) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("CREATE DATABASE \"" + DBName + "\""); // CREATE DATABASE "DBName"
                }
            }
        }
    }

    private static void ensureTableIsCreated(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS saved_games;");
            String createTableSql = "CREATE TABLE saved_games (" +
                    "id SERIAL PRIMARY KEY, " +
                    "save_name VARCHAR(255) NOT NULL UNIQUE, " +
                    "game_state_json JSONB NOT NULL, " +
                    "saved_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP" +
                    ");";
            stmt.execute(createTableSql);
        }
    }

    private static boolean isTableValid(Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet tables = metaData.getTables(null, null, "saved_games", null);
        if (!tables.next()) return false;

        List<String> required = new ArrayList<>(List.of("id", "save_name", "game_state_json", "saved_at"));
        ResultSet columns = metaData.getColumns(null, null, "saved_games", null);
        while(columns.next()) {
            required.remove(columns.getString("COLUMN_NAME"));
        }
        return required.isEmpty();
    }

    public static Connection getConnectionForSave(String DBName, String user, String password) throws SQLException {
        ensureDatabaseExists(DBName, user, password);
        Connection conn = DriverManager.getConnection(BASE_URL + DBName, user, password);
        try {
            if (!isTableValid(conn)) {
                ensureTableIsCreated(conn);
            }
        } catch (SQLException e) {
            conn.close();
            throw e;
        }
        return conn;
    }

    public static Connection getConnectionForLoad(String DBName, String user, String password) throws SQLException {
        Connection conn;
        try {
            conn = DriverManager.getConnection(BASE_URL + DBName, user, password);
        } catch (SQLException e) {
            throw new SQLException("Database '" + DBName + "' not found or connection failed.", e);
        }

        if (!isTableValid(conn)) {
            conn.close();
            throw new SQLException("The 'saved_games' table was not found or has an invalid structure.");
        }
        return conn;
    }
}