package com.auction.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
	
    private static final String URL = "jdbc:postgresql://ep-frosty-shape-a6qfou95.us-west-2.aws.neon.tech:5432/money_auction?sslmode=require";
    private static final String USER = "neondb_owner";
    private static final String PASSWORD = "npg_5riJRWDGS2UC";

    public static Connection getConnection() throws SQLException {
    	try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
