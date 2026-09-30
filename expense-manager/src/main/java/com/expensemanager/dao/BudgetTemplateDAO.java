package com.expensemanager.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

import com.expensemanager.util.DBConnection;

/**
 * Ported from Android BudgetTemplateDao — ONE shared category-amount
 * template used by every cash book. Reuses the budget_allocation_template
 * table with book_id fixed at 0 (a sentinel no real book has); the percent
 * column holds the flat amount, matching the Android approach exactly.
 */
public class BudgetTemplateDAO {

	private static final int GLOBAL_BOOK_ID = 0;

	private final DBConnection db = DBConnection.getInstance();

	/** category_id -> amount, for the config UI and for applying to a book. */
	public Map<Integer, BigDecimal> loadGlobalAmounts() throws SQLException {
		Map<Integer, BigDecimal> map = new LinkedHashMap<>();
		String sql = "SELECT category_id, percent FROM budget_allocation_template WHERE book_id = ?";
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, GLOBAL_BOOK_ID);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					map.put(rs.getInt(1), rs.getBigDecimal(2));
			}
		} finally {
			db.releaseConnection(conn);
		}
		return map;
	}

	public boolean hasGlobalTemplate() throws SQLException {
		String sql = "SELECT COUNT(*) FROM budget_allocation_template WHERE book_id = ?";
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, GLOBAL_BOOK_ID);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() && rs.getInt(1) > 0;
			}
		} finally {
			db.releaseConnection(conn);
		}
	}

	/** Replaces the whole template with the given category -> amount map. */
	public void saveGlobalTemplate(Map<Integer, BigDecimal> categoryAmounts) throws SQLException {
		Connection conn = db.getConnection();
		boolean prev = conn.getAutoCommit();
		try {
			conn.setAutoCommit(false);
			BigDecimal total = BigDecimal.ZERO;
			for (BigDecimal amt : categoryAmounts.values())
				total = total.add(amt);

			try (PreparedStatement del = conn
					.prepareStatement("DELETE FROM budget_allocation_template WHERE book_id = ?")) {
				del.setInt(1, GLOBAL_BOOK_ID);
				del.executeUpdate();
			}
			String ins = """
					INSERT INTO budget_allocation_template
					  (book_id, category_id, percent, default_overall_limit, updated_at)
					VALUES (?, ?, ?, ?, NOW())
					ON CONFLICT (book_id, category_id) DO UPDATE
					SET percent = EXCLUDED.percent, default_overall_limit = EXCLUDED.default_overall_limit,
					    updated_at = NOW()
					""";
			try (PreparedStatement ps = conn.prepareStatement(ins)) {
				for (Map.Entry<Integer, BigDecimal> e : categoryAmounts.entrySet()) {
					ps.setInt(1, GLOBAL_BOOK_ID);
					ps.setInt(2, e.getKey());
					ps.setBigDecimal(3, e.getValue());
					ps.setBigDecimal(4, total);
					ps.addBatch();
				}
				ps.executeBatch();
			}
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			throw e;
		} finally {
			conn.setAutoCommit(prev);
			db.releaseConnection(conn);
		}
	}

	/**
	 * Ported from Android applyTemplate: for the given book/month, add a
	 * budget category row for every template entry that doesn't already
	 * have its own row (existing rows are NOT overwritten), alertPct = 80.
	 *
	 * @return number of rows added
	 */
	public int applyToBook(int bookId, int year, int month) throws SQLException {
		Connection conn = db.getConnection();
		boolean prev = conn.getAutoCommit();
		try {
			conn.setAutoCommit(false);

			// Find or create the budget row for this book/month.
			Integer budgetId = null;
			BigDecimal overall = null;
			try (PreparedStatement ps = conn.prepareStatement(
					"SELECT id, overall_limit FROM budgets WHERE book_id = ? AND year = ? AND month = ?")) {
				ps.setInt(1, bookId);
				ps.setInt(2, year);
				ps.setInt(3, month);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						budgetId = rs.getInt(1);
						overall = rs.getBigDecimal(2);
					}
				}
			}
			if (budgetId == null) {
				try (PreparedStatement ps = conn.prepareStatement(
						"INSERT INTO budgets (book_id, year, month, overall_limit) VALUES (?, ?, ?, 0) RETURNING id")) {
					ps.setInt(1, bookId);
					ps.setInt(2, year);
					ps.setInt(3, month);
					try (ResultSet rs = ps.executeQuery()) {
						if (rs.next())
							budgetId = rs.getInt(1);
					}
				}
			}

			int added = 0;
			Map<Integer, BigDecimal> template = loadGlobalAmounts();
			if (template.isEmpty() || budgetId == null) {
				conn.commit();
				return 0;
			}

			// Existing rows are never overwritten (Android rule).
			Map<Integer, Boolean> existing = new LinkedHashMap<>();
			try (Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(
							"SELECT category_id FROM budget_categories WHERE budget_id = " + budgetId)) {
				while (rs.next())
					existing.put(rs.getInt(1), true);
			}

			String ins = "INSERT INTO budget_categories (budget_id, category_id, cat_limit, alert_pct) VALUES (?, ?, ?, 80)";
			try (PreparedStatement ps = conn.prepareStatement(ins)) {
				for (Map.Entry<Integer, BigDecimal> e : template.entrySet()) {
					if (existing.containsKey(e.getKey()))
						continue;
					ps.setInt(1, budgetId);
					ps.setInt(2, e.getKey());
					ps.setBigDecimal(3, e.getValue());
					ps.addBatch();
					added++;
				}
				ps.executeBatch();
			}

			conn.commit();
			return added;
		} catch (SQLException e) {
			conn.rollback();
			throw e;
		} finally {
			conn.setAutoCommit(prev);
			db.releaseConnection(conn);
		}
	}
}
