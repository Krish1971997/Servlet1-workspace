package com.chatapp.dao;

import com.chatapp.model.User;
import com.chatapp.util.DBUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // ── Registration ───────────────────────────────────────────
    /**
     * @return null on success, or an error message string
     */
    public String register(String username, String email, String password, String role) {
        // uniqueness check
        if (existsByUsername(username)) return "Username already taken.";
        if (existsByEmail(email))       return "Email already registered.";

        String hash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        String sql  = "INSERT INTO users (username, email, password, role) VALUES (?,?,?,?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, hash);
            ps.setString(4, role);
            ps.executeUpdate();
            return null; // success
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error: " + e.getMessage();
        }
    }

    // ── Authentication ─────────────────────────────────────────
    public User authenticate(String usernameOrEmail, String password) {
        String sql = "SELECT * FROM users WHERE (username=? OR email=?) AND is_active=TRUE";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usernameOrEmail);
            ps.setString(2, usernameOrEmail);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (BCrypt.checkpw(password, storedHash)) {
                        return mapRow(rs);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ── Lookup ─────────────────────────────────────────────────
    public User findById(int id) {
        return findBy("id", String.valueOf(id));
    }

    public User findByUsername(String username) {
        return findBy("username", username);
    }

    private User findBy(String col, String val) {
        String sql = "SELECT * FROM users WHERE " + col + "=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, val);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> findAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role='user' ORDER BY created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ── Uniqueness helpers ─────────────────────────────────────
    public boolean existsByUsername(String username) {
        return countBy("username", username) > 0;
    }

    public boolean existsByEmail(String email) {
        return countBy("email", email) > 0;
    }

    private int countBy(String col, String val) {
        String sql = "SELECT COUNT(*) FROM users WHERE " + col + "=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, val);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ── Activate / Deactivate ──────────────────────────────────
    public boolean setActive(int userId, boolean active) {
        String sql = "UPDATE users SET is_active=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, active);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ── Mapping ────────────────────────────────────────────────
    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        u.setActive(rs.getBoolean("is_active"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) u.setCreatedAt(ts.toLocalDateTime());
        return u;
    }
}
