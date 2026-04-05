package com.chatapp.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.chatapp.model.User;
import com.chatapp.util.DBUtil;

public class UserDAO {

	// ── Registration ───────────────────────────────────────────
	/**
	 * @return null on success, or an error message string
	 */

	public String insertBulkData(String role) {
		long startTime = System.currentTimeMillis();
		for (int i = 101; i <= 200; i++) {
			String username = "admin" + i;
			String email = "admin" + i + "@gmail.com";
			String password = username;
			// uniqueness check
			if (existsByUsername(username))
				return "Username already taken.";
			if (existsByEmail(email))
				return "Email already registered.";

			String hash = BCrypt.hashpw(password, BCrypt.gensalt(12));
			String sql = "INSERT INTO users (username, email, password, role) VALUES (?,?,?,?)";

			try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, username);
				ps.setString(2, email);
				ps.setString(3, hash);
				ps.setString(4, role);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				return "Database error: " + e.getMessage();
			}
		}
		long durationMillis = System.currentTimeMillis() - startTime;
		double durationMinutes = durationMillis / (1000.0 * 60);
		System.out.println("Time taken: " + durationMinutes + " minutes");

		return null;
	}

	public String register(String username, String email, String password, String role) {
		// uniqueness check
		if (existsByUsername(username))
			return "Username already taken.";
		if (existsByEmail(email))
			return "Email already registered.";

		String hash = BCrypt.hashpw(password, BCrypt.gensalt(12));
		String sql = "INSERT INTO users (username, email, password, role) VALUES (?,?,?,?)";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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
		String sql = "SELECT * FROM users WHERE (username=? OR email=?)";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, val);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return mapRow(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<User> findAllUsers() {
		List<User> list = new ArrayList<>();
		String sql = "SELECT * FROM users ORDER BY id asc"; // WHERE role='user'
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next())
				list.add(mapRow(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int countAllUsers() {
	    String sql = "SELECT COUNT(*) FROM users";
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) return rs.getInt(1);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return 0;
	}
	
	public List<User> findAllUsersPaginated(int page, int pageSize) {
	    String sql = "SELECT * FROM users ORDER BY id LIMIT ? OFFSET ?";
	    List<User> users = new ArrayList<>();
	    int offset = (page - 1) * pageSize;
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, pageSize);
	        ps.setInt(2, offset);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            users.add(mapRow(rs));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return users;
	}

	public User findUser(int id) {
		String sql = "SELECT * FROM users WHERE id=?"; // WHERE role='user'
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				return mapRow(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
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
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, val);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// ── Activate / Deactivate ──────────────────────────────────
	public boolean setActive(int userId, boolean active) {
		String sql = "UPDATE users SET is_active=? WHERE id=?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setBoolean(1, active);
			ps.setInt(2, userId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean DeleteUser(int userId) {
		String sql = "Delete From users WHERE id=?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userId);
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
		if (ts != null)
			u.setCreatedAt(ts.toLocalDateTime());
		return u;
	}

	public void generatePassword(String role) throws IOException {
		File file= new File("C:\\Users\\Admin\\Downloads\\ChatApp\\password.txt");
		FileWriter writer = new FileWriter(file);
		long startTime = System.currentTimeMillis();
		System.out.println(file.getAbsolutePath());
		String sql = "INSERT INTO users (username, email, password, role) ";
		for (int i = 205; i <= 20000; i++) {
			String username = "admin" + i;
			String hash = BCrypt.hashpw(username, BCrypt.gensalt(12));
			String email = "admin" + i + "@gmail.com";
			writer.write(sql+"\n");
			writer.write("values ('"+username+"', '"+email+"', '"+hash+"', '"+role+"');");
			writer.write("\n");
//			writer.write(username + ", " + hash);
		}
		writer.close();
		System.out.println("Text written successfully.");
		long durationMillis = System.currentTimeMillis() - startTime;
		double durationMinutes = durationMillis / (1000.0 * 60);
		System.out.println("Time taken: " + durationMinutes + " minutes");
	}
}
