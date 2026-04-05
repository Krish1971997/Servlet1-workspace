package com.chatapp.dao;

import com.chatapp.model.Group;
import com.chatapp.model.GroupMessage;
import com.chatapp.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO {

	// ── Create group ───────────────────────────────────────────
	public int createGroup(String name, String description, int createdBy) {
		String sql = "INSERT INTO groups (name, description, created_by) VALUES (?,?,?) RETURNING id";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, name);
			ps.setString(2, description);
			ps.setInt(3, createdBy);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					int gid = rs.getInt(1);
					addMember(gid, createdBy); // creator auto-joins
					return gid;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	// ── Membership ─────────────────────────────────────────────
	public boolean addMember(int groupId, int userId) {
		String sql = "INSERT INTO group_members (group_id, user_id) VALUES (?,?) ON CONFLICT DO NOTHING";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, groupId);
			ps.setInt(2, userId);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean removeMember(int groupId, int userId) {
		String sql = "DELETE FROM group_members WHERE group_id=? AND user_id=?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, groupId);
			ps.setInt(2, userId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isMember(int groupId, int userId) {
		String sql = "SELECT 1 FROM group_members WHERE group_id=? AND user_id=?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, groupId);
			ps.setInt(2, userId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// ── List groups ────────────────────────────────────────────
	public List<Group> getAllGroups() {
		return queryGroups("SELECT g.*, u.username AS created_by_username "
				+ "FROM groups g JOIN users u ON g.created_by=u.id " + "ORDER BY g.created_at DESC", null);
	}

	public List<Group> getGroupsForUser(int userId) {
		return queryGroups("SELECT g.*, u.username AS created_by_username "
				+ "FROM groups g JOIN users u ON g.created_by=u.id " + "JOIN group_members gm ON gm.group_id=g.id "
				+ "WHERE gm.user_id=? ORDER BY g.created_at DESC", userId);
	}

	private List<Group> queryGroups(String sql, Integer param) {
		List<Group> list = new ArrayList<>();
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			if (param != null)
				ps.setInt(1, param);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					list.add(mapGroup(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public Group findById(int id) {
		String sql = "SELECT g.*, u.username AS created_by_username "
				+ "FROM groups g JOIN users u ON g.created_by=u.id WHERE g.id=?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return mapGroup(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ── Group Chat Messages ────────────────────────────────────
	public boolean sendMessage(int groupId, int senderId, String message) {
		String sql = "INSERT INTO group_messages (group_id, sender_id, message) VALUES (?,?,?)";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, groupId);
			ps.setInt(2, senderId);
			ps.setString(3, message);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<GroupMessage> getMessages(int groupId, boolean isAdmin) {
		String sql = "SELECT gm.*, u.username AS sender_username "
				+ "FROM group_messages gm JOIN users u ON gm.sender_id=u.id " + "WHERE gm.group_id=?"
				+ (isAdmin ? "" : " AND gm.is_hidden=FALSE") + " ORDER BY gm.created_at ASC";
		List<GroupMessage> list = new ArrayList<>();
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, groupId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					list.add(mapMessage(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** Fetch only messages after a given ID (for polling). */
	public List<GroupMessage> getNewMessages(int groupId, int afterId, boolean isAdmin) {
		String sql = "SELECT gm.*, u.username AS sender_username "
				+ "FROM group_messages gm JOIN users u ON gm.sender_id=u.id " + "WHERE gm.group_id=? AND gm.id>?"
				+ (isAdmin ? "" : " AND gm.is_hidden=FALSE") + " ORDER BY gm.created_at ASC";
		List<GroupMessage> list = new ArrayList<>();
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, groupId);
			ps.setInt(2, afterId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					list.add(mapMessage(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean hideMessage(int msgId, boolean hidden) {
		String sql = "UPDATE group_messages SET is_hidden=? WHERE id=?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setBoolean(1, hidden);
			ps.setInt(2, msgId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// ── Members list ───────────────────────────────────────────
	public List<Integer> getMemberIds(int groupId) {
		List<Integer> ids = new ArrayList<>();
		String sql = "SELECT user_id FROM group_members WHERE group_id=?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, groupId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					ids.add(rs.getInt("user_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ids;
	}

	// ── Mapping ────────────────────────────────────────────────
	private Group mapGroup(ResultSet rs) throws SQLException {
		Group g = new Group();
		g.setId(rs.getInt("id"));
		g.setName(rs.getString("name"));
		g.setDescription(rs.getString("description"));
		g.setCreatedBy(rs.getInt("created_by"));
		g.setCreatedByUsername(rs.getString("created_by_username"));
		Timestamp ts = rs.getTimestamp("created_at");
		if (ts != null)
			g.setCreatedAt(ts.toLocalDateTime());
		return g;
	}

	private GroupMessage mapMessage(ResultSet rs) throws SQLException {
		GroupMessage m = new GroupMessage();
		m.setId(rs.getInt("id"));
		m.setGroupId(rs.getInt("group_id"));
		m.setSenderId(rs.getInt("sender_id"));
		m.setSenderUsername(rs.getString("sender_username"));
		m.setMessage(rs.getString("message"));
		m.setHidden(rs.getBoolean("is_hidden"));
		Timestamp ts = rs.getTimestamp("created_at");
		if (ts != null)
			m.setCreatedAt(ts.toLocalDateTime());
		return m;
	}
}
