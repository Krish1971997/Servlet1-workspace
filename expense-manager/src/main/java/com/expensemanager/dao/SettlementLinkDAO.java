package com.expensemanager.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.expensemanager.model.SettlementLink;
import com.expensemanager.util.DBConnection;

/**
 * Ported from Android SettlementLinkDao — partial-amount links between a
 * settlement transaction and the transactions it settles.
 */
public class SettlementLinkDAO {

	private final DBConnection db = DBConnection.getInstance();

	/** All links touching this transaction, whichever side it's on. */
	public List<SettlementLink> findForTransaction(int txnId) throws SQLException {
		List<SettlementLink> list = new ArrayList<>();
		String sql = """
				SELECT sl.id, sl.settlement_txn_id, sl.linked_txn_id, sl.amount
				FROM settlement_links sl
				WHERE sl.settlement_txn_id = ? OR sl.linked_txn_id = ?
				ORDER BY sl.id DESC
				""";
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, txnId);
			ps.setInt(2, txnId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					SettlementLink l = new SettlementLink();
					l.setId(rs.getLong(1));
					l.setSettlementTxnId(rs.getInt(2));
					l.setLinkedTxnId(rs.getInt(3));
					l.setAmount(rs.getBigDecimal(4));
					list.add(l);
				}
			}
		} finally {
			db.releaseConnection(conn);
		}
		return list;
	}

	/** Total already linked away from this transaction (both directions). */
	public BigDecimal sumLinkedFor(int txnId) throws SQLException {
		String sql = "SELECT COALESCE(SUM(amount), 0) FROM settlement_links WHERE settlement_txn_id = ? OR linked_txn_id = ?";
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, txnId);
			ps.setInt(2, txnId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
			}
		} finally {
			db.releaseConnection(conn);
		}
	}

	public void insert(int fromTxnId, int toTxnId, BigDecimal amount) throws SQLException {
		String sql = "INSERT INTO settlement_links (settlement_txn_id, linked_txn_id, amount) VALUES (?, ?, ?)";
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, fromTxnId);
			ps.setInt(2, toTxnId);
			ps.setBigDecimal(3, amount);
			ps.executeUpdate();
		} finally {
			db.releaseConnection(conn);
		}
	}

	public void delete(long linkId) throws SQLException {
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement("DELETE FROM settlement_links WHERE id = ?")) {
			ps.setLong(1, linkId);
			ps.executeUpdate();
		} finally {
			db.releaseConnection(conn);
		}
	}
}
