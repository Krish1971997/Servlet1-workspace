package com.expensemanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.expensemanager.model.PaymentType;
import com.expensemanager.util.DBConnection;

/**
 * Ported from Android PaymentTypeDao — payment types with a single default.
 * Transactions keep the payment type as plain text, so deleting a type only
 * removes it from future selection.
 */
public class PaymentTypeDAO {

	private final DBConnection db = DBConnection.getInstance();

	public List<PaymentType> findAll() throws SQLException {
		List<PaymentType> list = new ArrayList<>();
		String sql = "SELECT id, name, is_default FROM payment_types ORDER BY is_default DESC, name";
		Connection conn = db.getConnection();
		try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
			while (rs.next())
				list.add(new PaymentType(rs.getInt(1), rs.getString(2), rs.getBoolean(3)));
		} finally {
			db.releaseConnection(conn);
		}
		return list;
	}

	/** Marks exactly one payment type as default, clearing any previous one. */
	public void setDefault(int id) throws SQLException {
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement("UPDATE payment_types SET is_default = (id = ?)")) {
			ps.setInt(1, id);
			ps.executeUpdate();
		} finally {
			db.releaseConnection(conn);
		}
	}

	public void insert(String name) throws SQLException {
		String trimmed = name.trim();
		if (trimmed.isEmpty())
			return;
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(
				"INSERT INTO payment_types (name) VALUES (?) ON CONFLICT (name) DO NOTHING")) {
			ps.setString(1, trimmed);
			ps.executeUpdate();
		} finally {
			db.releaseConnection(conn);
		}
	}

	public void update(int id, String newName) throws SQLException {
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement("UPDATE payment_types SET name = ?, updated_at = NOW() WHERE id = ?")) {
			ps.setString(1, newName.trim());
			ps.setInt(2, id);
			ps.executeUpdate();
		} finally {
			db.releaseConnection(conn);
		}
	}

	public void delete(int id) throws SQLException {
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement("DELETE FROM payment_types WHERE id = ?")) {
			ps.setInt(1, id);
			ps.executeUpdate();
		} finally {
			db.releaseConnection(conn);
		}
	}
}
