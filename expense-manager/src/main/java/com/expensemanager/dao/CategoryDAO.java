package com.expensemanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.expensemanager.model.Category;
import com.expensemanager.util.DBConnection;

public class CategoryDAO {

	private static final Logger log = LoggerFactory.getLogger(CategoryDAO.class);
	
    private final DBConnection db = DBConnection.getInstance();
    
    public List<Category> findByType(String type) throws SQLException {
        String sql = "SELECT id, name, type FROM categories WHERE type=?::txn_type ORDER BY name";
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            List<Category> list = new ArrayList<>();
            while (rs.next())
                list.add(new Category(rs.getInt("id"), rs.getString("name"), rs.getString("type")));
            return list;
        } finally {
            db.releaseConnection(conn);
        }
    }

    public void insert(String name, String type) throws SQLException {
        String sql = "INSERT INTO categories (name, type) VALUES (?, ?::txn_type) ON CONFLICT DO NOTHING";
        Connection conn = db.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, type);
            ps.executeUpdate();
        } finally {
            db.releaseConnection(conn);
        }
    }
}
