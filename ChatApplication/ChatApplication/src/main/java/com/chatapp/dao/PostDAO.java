package com.chatapp.dao;

import com.chatapp.model.Post;
import com.chatapp.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

	// ── Create ─────────────────────────────────────────────────
	public int createPost(int authorId, Integer groupId, String content) {
		String sql = "INSERT INTO posts (author_id, group_id, content) VALUES (?,?,?) RETURNING id";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, authorId);
			if (groupId != null)
				ps.setInt(2, groupId);
			else
				ps.setNull(2, Types.INTEGER);
			ps.setString(3, content);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	// ── Wall posts (no group) visible to caller ────────────────
	/**
	 * Returns wall posts. - If caller is admin: all non-hidden + own hidden. - If
	 * caller is regular user: non-hidden + posts shared with caller.
	 */
	public List<Post> getWallPosts(int callerId, boolean isAdmin) {
		String sql = isAdmin
				? "SELECT p.*, u.username AS author_username " + "FROM posts p JOIN users u ON p.author_id=u.id "
						+ "WHERE p.group_id IS NULL " + "ORDER BY p.created_at DESC"
				: "SELECT p.*, u.username AS author_username " + "FROM posts p JOIN users u ON p.author_id=u.id "
						+ "WHERE p.group_id IS NULL AND p.is_hidden=FALSE "
						+ "AND (NOT EXISTS (SELECT 1 FROM post_shares WHERE post_id=p.id) "
						+ "     OR EXISTS (SELECT 1 FROM post_shares WHERE post_id=p.id AND user_id=?)) "
						+ "ORDER BY p.created_at DESC";

		List<Post> list = new ArrayList<>();
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			if (!isAdmin)
				ps.setInt(1, callerId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					list.add(mapRow(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	// ── Group posts ────────────────────────────────────────────
	public List<Post> getGroupPosts(int groupId, int callerId, boolean isAdmin) {
		String sql = isAdmin
				? "SELECT p.*, u.username AS author_username " + "FROM posts p JOIN users u ON p.author_id=u.id "
						+ "WHERE p.group_id=? ORDER BY p.created_at DESC"
				: "SELECT p.*, u.username AS author_username " + "FROM posts p JOIN users u ON p.author_id=u.id "
						+ "WHERE p.group_id=? AND p.is_hidden=FALSE "
						+ "AND (NOT EXISTS (SELECT 1 FROM post_shares WHERE post_id=p.id) "
						+ "     OR EXISTS (SELECT 1 FROM post_shares WHERE post_id=p.id AND user_id=?)) "
						+ "ORDER BY p.created_at DESC";

		List<Post> list = new ArrayList<>();
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, groupId);
			if (!isAdmin)
				ps.setInt(2, callerId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					list.add(mapRow(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	// ── Hide / Unhide ──────────────────────────────────────────
	public boolean setHidden(int postId, boolean hidden) {
		String sql = "UPDATE posts SET is_hidden=? WHERE id=?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setBoolean(1, hidden);
			ps.setInt(2, postId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// ── Share post with specific users (admin feature) ─────────
	public boolean sharePost(int postId, List<Integer> userIds) {
		String delSql = "DELETE FROM post_shares WHERE post_id=?";
		String insSql = "INSERT INTO post_shares (post_id, user_id) VALUES (?,?) ON CONFLICT DO NOTHING";
		try (Connection conn = DBUtil.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement del = conn.prepareStatement(delSql)) {
				del.setInt(1, postId);
				del.executeUpdate();
			}
			try (PreparedStatement ins = conn.prepareStatement(insSql)) {
				for (int uid : userIds) {
					ins.setInt(1, postId);
					ins.setInt(2, uid);
					ins.addBatch();
				}
				ins.executeBatch();
			}
			conn.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// ── Delete ─────────────────────────────────────────────────
	public boolean deletePost(int postId) {
		String sql = "DELETE FROM posts WHERE id=?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, postId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// ── Find by ID ─────────────────────────────────────────────
	public Post findById(int id) {
		String sql = "SELECT p.*, u.username AS author_username "
				+ "FROM posts p JOIN users u ON p.author_id=u.id WHERE p.id=?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return mapRow(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ── Mapping ────────────────────────────────────────────────
	private Post mapRow(ResultSet rs) throws SQLException {
		Post p = new Post();
		p.setId(rs.getInt("id"));
		p.setAuthorId(rs.getInt("author_id"));
		p.setAuthorUsername(rs.getString("author_username"));
		int gid = rs.getInt("group_id");
		p.setGroupId(rs.wasNull() ? null : gid);
		p.setContent(rs.getString("content"));
		p.setHidden(rs.getBoolean("is_hidden"));
		Timestamp ts = rs.getTimestamp("created_at");
		if (ts != null)
			p.setCreatedAt(ts.toLocalDateTime());
		return p;
	}
}
