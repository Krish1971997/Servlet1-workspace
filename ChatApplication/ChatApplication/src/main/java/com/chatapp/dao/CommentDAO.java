package com.chatapp.dao;

import com.chatapp.model.Comment;
import com.chatapp.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    // ── Create ─────────────────────────────────────────────────
    public boolean addComment(int postId, int authorId, String content) {
        String sql = "INSERT INTO comments (post_id, author_id, content) VALUES (?,?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, postId); ps.setInt(2, authorId); ps.setString(3, content);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ── Read ───────────────────────────────────────────────────
    /**
     * Returns comments for a post.
     * Regular users only see non-hidden. Admins see all.
     */
    public List<Comment> getComments(int postId, boolean isAdmin) {
        String sql = "SELECT c.*, u.username AS author_username " +
                     "FROM comments c JOIN users u ON c.author_id=u.id " +
                     "WHERE c.post_id=?" +
                     (isAdmin ? "" : " AND c.is_hidden=FALSE") +
                     " ORDER BY c.created_at ASC";
        List<Comment> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, postId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── Hide / Unhide (admin) ──────────────────────────────────
    public boolean setHidden(int commentId, boolean hidden) {
        String sql = "UPDATE comments SET is_hidden=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, hidden); ps.setInt(2, commentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ── Delete ─────────────────────────────────────────────────
    public boolean deleteComment(int commentId) {
        String sql = "DELETE FROM comments WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ── Mapping ────────────────────────────────────────────────
    private Comment mapRow(ResultSet rs) throws SQLException {
        Comment c = new Comment();
        c.setId(rs.getInt("id"));
        c.setPostId(rs.getInt("post_id"));
        c.setAuthorId(rs.getInt("author_id"));
        c.setAuthorUsername(rs.getString("author_username"));
        c.setContent(rs.getString("content"));
        c.setHidden(rs.getBoolean("is_hidden"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) c.setCreatedAt(ts.toLocalDateTime());
        return c;
    }
}
