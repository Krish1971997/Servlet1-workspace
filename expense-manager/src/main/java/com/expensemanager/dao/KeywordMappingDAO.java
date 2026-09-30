package com.expensemanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.expensemanager.model.KeywordMapping;
import com.expensemanager.util.DBConnection;

/**
 * Ported from Android KeywordMappingDao — description keyword → category /
 * sub-category mappings. Used by the add-transaction screen to auto-suggest a
 * category while the user types the note, and by the note autocomplete
 * (past matching notes).
 */
public class KeywordMappingDAO {

	private final DBConnection db = DBConnection.getInstance();

	public List<KeywordMapping> findAll() throws SQLException {
		List<KeywordMapping> list = new ArrayList<>();
		String sql = """
				SELECT k.id, k.keyword, k.type, k.category_id, c.name, k.sub_category_id, sc.name, k.book_id
				FROM keyword_mappings k
				JOIN categories c ON c.id = k.category_id
				LEFT JOIN sub_categories sc ON sc.sub_categories_id = k.sub_category_id
				ORDER BY k.keyword
				""";
		Connection conn = db.getConnection();
		try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				KeywordMapping m = new KeywordMapping();
				m.setId(rs.getInt(1));
				m.setKeyword(rs.getString(2));
				m.setType(rs.getString(3));
				m.setCategoryId(rs.getInt(4));
				m.setCategoryName(rs.getString(5));
				int subId = rs.getInt(6);
				m.setSubCategoryId(rs.wasNull() ? null : subId);
				m.setSubCategoryName(rs.getString(7));
				int bid = rs.getInt(8);
				m.setBookId(rs.wasNull() ? null : bid);
				list.add(m);
			}
		} finally {
			db.releaseConnection(conn);
		}
		return list;
	}

	/**
	 * Longest keyword occurring in the given note wins (Android rule); on equal
	 * length a book-specific mapping beats a common one.
	 */
	public KeywordMapping suggest(String note, String type, Integer bookId) throws SQLException {
		if (note == null || note.trim().length() < 3)
			return null;
		String sql = """
				SELECT k.id, k.keyword, k.type, k.category_id, c.name, k.sub_category_id, sc.name, k.book_id
				FROM keyword_mappings k
				JOIN categories c ON c.id = k.category_id
				LEFT JOIN sub_categories sc ON sc.sub_categories_id = k.sub_category_id
				WHERE k.type = ?::txn_type
				  AND (k.book_id IS NULL OR k.book_id = ?)
				  AND ? ILIKE '%' || k.keyword || '%'
				ORDER BY LENGTH(k.keyword) DESC, (k.book_id IS NOT NULL) DESC
				LIMIT 1
				""";
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, type);
			if (bookId != null)
				ps.setInt(2, bookId);
			else
				ps.setNull(2, java.sql.Types.INTEGER);
			ps.setString(3, note.trim());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					KeywordMapping m = new KeywordMapping();
					m.setId(rs.getInt(1));
					m.setKeyword(rs.getString(2));
					m.setType(rs.getString(3));
					m.setCategoryId(rs.getInt(4));
					m.setCategoryName(rs.getString(5));
					int subId = rs.getInt(6);
					m.setSubCategoryId(rs.wasNull() ? null : subId);
					m.setSubCategoryName(rs.getString(7));
					int bid = rs.getInt(8);
					m.setBookId(rs.wasNull() ? null : bid);
					return m;
				}
			}
		} finally {
			db.releaseConnection(conn);
		}
		return null;
	}

	/** Past distinct notes matching the typed text — note autocomplete. */
	public List<String> suggestNotes(String typed, Integer bookId) throws SQLException {
		List<String> list = new ArrayList<>();
		if (typed == null || typed.trim().length() < 2)
			return list;
		String sql = "SELECT DISTINCT note FROM transactions "
				+ "WHERE note ILIKE ? AND note IS NOT NULL "
				+ (bookId != null ? "AND book_id = ? " : "")
				+ "ORDER BY note LIMIT 8";
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, "%" + typed.trim() + "%");
			if (bookId != null)
				ps.setInt(2, bookId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					list.add(rs.getString(1));
			}
		} finally {
			db.releaseConnection(conn);
		}
		return list;
	}

	public void insert(String keyword, String type, int categoryId, Integer subCategoryId, Integer bookId)
			throws SQLException {
		String trimmed = keyword.trim();
		if (trimmed.isEmpty())
			return;
		String sql = """
				INSERT INTO keyword_mappings (keyword, type, category_id, sub_category_id, book_id)
				VALUES (?, ?::txn_type, ?, ?, ?)
				ON CONFLICT DO NOTHING
				""";
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, trimmed);
			ps.setString(2, type);
			ps.setInt(3, categoryId);
			if (subCategoryId != null)
				ps.setInt(4, subCategoryId);
			else
				ps.setNull(4, java.sql.Types.INTEGER);
			if (bookId != null)
				ps.setInt(5, bookId);
			else
				ps.setNull(5, java.sql.Types.INTEGER);
			ps.executeUpdate();
		} finally {
			db.releaseConnection(conn);
		}
	}

	public void delete(int id) throws SQLException {
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement("DELETE FROM keyword_mappings WHERE id = ?")) {
			ps.setInt(1, id);
			ps.executeUpdate();
		} finally {
			db.releaseConnection(conn);
		}
	}
}
