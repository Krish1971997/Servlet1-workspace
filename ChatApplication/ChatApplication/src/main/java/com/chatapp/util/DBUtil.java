package com.chatapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages PostgreSQL JDBC connections.
 * Configure DB_URL / DB_USER / DB_PASS for your environment.
 */
public class DBUtil {

    private static final String DB_URL  = "jdbc:postgresql://localhost:5432/chatapp";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "postgres";   // ← change this

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("PostgreSQL driver not found: " + e.getMessage());
        }
    }

    /** Returns a new connection from DriverManager (replace with a pool in production). */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    /** Quietly closes a connection. */
    public static void close(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException ignored) {}
        }
    }
}
