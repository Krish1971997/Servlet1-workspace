package com.expensemanager.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.expensemanager.util.DBConnection;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Ported from Android RecycleBinDao — soft-delete with full restore. Every
 * tracked delete() snapshots the row (plus its child rows) as JSON into
 * recycle_bin before deleting; restore() re-inserts the row with its
 * original id and its children, then drops the bin entry.
 */
public class RecycleBinDAO {

	private final DBConnection db = DBConnection.getInstance();

	/** Child-array keys embedded in the snapshot JSON, mapped to real tables. */
	private static final Map<String, String> CHILD_TABLES = Map.of(
			"receipts_data", "transaction_receipts",
			"custom_values_data", "transaction_custom_values",
			"audit_data", "transaction_audit_log",
			"sub_categories_data", "sub_categories",
			"categories_data", "categories",
			"budget_categories_data", "budget_categories");

	/** Snapshot one row as JSON. Returns null when the row doesn't exist. */
	public static JsonObject snapshotRow(Connection conn, String table, int id) throws SQLException {
		String sql = "SELECT row_to_json(t) FROM " + table + " t WHERE id = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String j = rs.getString(1);
					return j != null ? JsonParser.parseString(j).getAsJsonObject() : null;
				}
			}
		}
		return null;
	}

	/** Snapshot child rows (the SQL must return row_to_json(t) rows). */
	public static JsonArray snapshotChildren(Connection conn, String sql, int id) throws SQLException {
		JsonArray arr = new JsonArray();
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String j = rs.getString(1);
					if (j != null)
						arr.add(JsonParser.parseString(j).getAsJsonObject());
				}
			}
		}
		return arr;
	}

	/** Store a snapshot in the bin. Call BEFORE the DELETE, same transaction. */
	public void put(Connection conn, String tableName, int recordId, Integer bookId, JsonObject row)
			throws SQLException {
		if (row == null)
			return;
		String sql = """
				INSERT INTO recycle_bin (table_name, record_id, book_id, record_json, deleted_at)
				VALUES (?, ?, ?, ?::jsonb, NOW())
				ON CONFLICT (table_name, record_id)
				DO UPDATE SET record_json = EXCLUDED.record_json, book_id = EXCLUDED.book_id, deleted_at = NOW()
				""";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, tableName);
			ps.setInt(2, recordId);
			if (bookId != null)
				ps.setInt(3, bookId);
			else
				ps.setNull(3, Types.INTEGER);
			ps.setString(4, row.toString());
			ps.executeUpdate();
		}
	}

	/** Bin listing for the active book (plus common rows without a book). */
	public List<Map<String, Object>> findAll(Integer bookId) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<>();
		String sql = """
				SELECT id, table_name, record_id, book_id,
				       COALESCE(record_json::jsonb->>'name', record_json::jsonb->>'note', '#' || record_id) AS label,
				       deleted_at
				FROM recycle_bin
				""" + (bookId != null ? "WHERE book_id = ? OR book_id IS NULL " : "")
				+ "ORDER BY deleted_at DESC LIMIT 200";
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			if (bookId != null)
				ps.setInt(1, bookId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> m = new LinkedHashMap<>();
					m.put("id", rs.getLong(1));
					m.put("tableName", rs.getString(2));
					m.put("recordId", rs.getInt(3));
					m.put("label", rs.getString(5));
					m.put("deletedAt", rs.getTimestamp(6));
					list.add(m);
				}
			}
		} finally {
			db.releaseConnection(conn);
		}
		return list;
	}

	/** Re-insert the original row (explicit id, ON CONFLICT skip) + children. */
	public boolean restore(long binId) throws SQLException {
		Connection conn = db.getConnection();
		boolean prev = conn.getAutoCommit();
		try {
			conn.setAutoCommit(false);

			String table;
			JsonObject row;
			String sql = "SELECT table_name, record_json FROM recycle_bin WHERE id = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setLong(1, binId);
				try (ResultSet rs = ps.executeQuery()) {
					if (!rs.next()) {
						conn.rollback();
						return false;
					}
					table = rs.getString(1);
					String j = rs.getString(2);
					row = j != null ? JsonParser.parseString(j).getAsJsonObject() : null;
				}
			}
			if (row == null) {
				conn.rollback();
				return false;
			}

			reinsert(conn, table, row);
			for (Map.Entry<String, String> child : CHILD_TABLES.entrySet()) {
				if (row.has(child.getKey()) && row.get(child.getKey()).isJsonArray()) {
					for (JsonElement el : row.getAsJsonArray(child.getKey()))
						reinsert(conn, child.getValue(), el.getAsJsonObject());
				}
			}

			try (PreparedStatement ps = conn.prepareStatement("DELETE FROM recycle_bin WHERE id = ?")) {
				ps.setLong(1, binId);
				ps.executeUpdate();
			}
			conn.commit();
			return true;
		} catch (Exception e) {
			conn.rollback();
			org.slf4j.LoggerFactory.getLogger(RecycleBinDAO.class).error("restore failed for binId={}", binId, e);
			return false;
		} finally {
			conn.setAutoCommit(prev);
			db.releaseConnection(conn);
		}
	}

	/** Remove the bin entry without restoring (permanent purge). */
	public void purge(long binId) throws SQLException {
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement("DELETE FROM recycle_bin WHERE id = ?")) {
			ps.setLong(1, binId);
			ps.executeUpdate();
		} finally {
			db.releaseConnection(conn);
		}
	}

	private void reinsert(Connection conn, String table, JsonObject row) throws SQLException {
		if (row == null || !row.has("id") || row.get("id").isJsonNull())
			return;

		List<String> cols = new ArrayList<>();
		for (Map.Entry<String, JsonElement> e : row.entrySet()) {
			if (e.getValue().isJsonNull())
				continue;
			if (CHILD_TABLES.containsKey(e.getKey()))
				continue;
			cols.add(e.getKey());
		}
		if (cols.isEmpty())
			return;

		// Real column types → explicit casts so JSON strings (timestamps,
		// enums) re-insert cleanly.
		Map<String, String> types = columnTypes(conn, table);
		StringBuilder sql = new StringBuilder("INSERT INTO ").append(table).append(" (");
		StringBuilder marks = new StringBuilder(" VALUES (");
		for (int i = 0; i < cols.size(); i++) {
			String col = cols.get(i);
			String dt = types.getOrDefault(col, "");
			String cast;
			if ("txn_type".equals(dt))
				cast = "?::txn_type";
			else if ("timestamp without time zone".equals(dt))
				cast = "?::timestamp";
			else if ("timestamp with time zone".equals(dt))
				cast = "?::timestamptz";
			else if ("date".equals(dt))
				cast = "?::date";
			else
				cast = "?";
			sql.append(i > 0 ? ", " : "").append(col);
			marks.append(i > 0 ? ", " : "").append(cast);
		}
		sql.append(")").append(marks).append(") ON CONFLICT (id) DO NOTHING");

		try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
			int i = 1;
			for (String col : cols) {
				JsonElement el = row.get(col);
				if (el.isJsonNull())
					ps.setNull(i++, Types.VARCHAR);
				else if (el.getAsJsonPrimitive().isBoolean())
					ps.setBoolean(i++, el.getAsBoolean());
				else if (el.getAsJsonPrimitive().isNumber()) {
					String num = el.getAsString();
					if (num.contains("."))
						ps.setBigDecimal(i++, new BigDecimal(num));
					else
						ps.setLong(i++, el.getAsLong());
				} else
					ps.setString(i++, el.getAsString());
			}
			ps.executeUpdate();
		}
	}

	private Map<String, String> columnTypes(Connection conn, String table) throws SQLException {
		Map<String, String> map = new LinkedHashMap<>();
		String sql = "SELECT column_name, udt_name, data_type FROM information_schema.columns "
				+ "WHERE table_schema = 'public' AND table_name = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, table);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String udt = rs.getString(2);
					map.put(rs.getString(1), "txn_type".equals(udt) ? "txn_type" : rs.getString(3));
				}
			}
		}
		return map;
	}
}
