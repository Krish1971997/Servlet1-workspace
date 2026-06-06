package com.expensemanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.expensemanager.model.ColumnDefinition;
import com.expensemanager.util.DBConnection;

public class ColumnDefinitionDAO {
	private static final Logger log = LoggerFactory.getLogger(ColumnDefinitionDAO.class);
    private final DBConnection db = DBConnection.getInstance();

    public List<ColumnDefinition> findByType(String type) throws SQLException {
        String sql = "SELECT id, col_name, col_key, type FROM column_definitions WHERE type=?::txn_type ORDER BY id";
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            List<ColumnDefinition> list = new ArrayList<>();
            while (rs.next())
                list.add(new ColumnDefinition(rs.getInt("id"), rs.getString("col_name"),
                        rs.getString("col_key"), rs.getString("type")));
            return list;
        } finally {
            db.releaseConnection(conn);
        }
    }

    public void insert(String colName, String type) throws SQLException {
        // Convert "Invoice Number" → "invoice_number"
        String colKey = colName.trim().toLowerCase()
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_|_$", "");
        String sql = """
            INSERT INTO column_definitions (col_name, col_key, type)
            VALUES (?, ?, ?::txn_type)
            ON CONFLICT (col_key, type) DO NOTHING
            """;
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, colName.trim());
            ps.setString(2, colKey);
            ps.setString(3, type);
            ps.executeUpdate();
        } finally {
            db.releaseConnection(conn);
        }
    }
}
