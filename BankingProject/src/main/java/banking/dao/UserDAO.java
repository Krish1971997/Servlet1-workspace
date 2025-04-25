package banking.dao;

import banking.models.User;
import banking.dao.AccountDAO;
import banking.utils.MySQLConnection;
import banking.exceptions.UserNotFoundException;
import banking.interfaces.UserManager;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO implements UserManager {

	public long createUser(User user, String passwd) throws SQLException {
		long res = 0;
		String query = "INSERT INTO users (user_id, name, address, phone_number, passwd) VALUES (?, ?, ?, ?, ?)";
		try {

			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, user.getUserId());
			stmt.setString(2, user.getName());
			stmt.setString(3, user.getAddress());
			stmt.setString(4, user.getPhoneNumber());
			stmt.setString(5, passwd);
			res = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;

	}

	public int DeleteUser(String uid) {
		String query = "DELETE FROM users WHERE user_id = ? ";
		String account = "SELECT * FROM accounts WHERE user_id = ?";
		int res = 0;
		AccountDAO accountDAO = new AccountDAO();
		String aid = "", type = "";
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(account);
			stmt.setString(1, uid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				aid = rs.getString("account_id");
				type = rs.getString("type");
				if (type.equals("savings")) {
					res = accountDAO.deleteSavingsAccount(aid);
				} else {
					res = accountDAO.deleteCheckingsAccount(aid);
				}
			}
			stmt = conn.prepareStatement(query);
			stmt.setString(1, uid);
			res = stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public List<User> getAllUsers() throws SQLException {
		String query = "SELECT * FROM users";
		List<User> res = new ArrayList<>();
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			try {
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					User user = new User(rs.getString("user_id"), rs.getString("name"), rs.getString("address"),
							rs.getString("phone_number"));
					res.add(user);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	public String getPasswd(String user_id) throws SQLException {
		String query = "SELECT passwd FROM users WHERE user_id = ?";
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, user_id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getString("passwd");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getMD5(String passwd) throws SQLException {
		String query = "SELECT MD5( ? ) AS passwd";
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, passwd);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getString("passwd");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void UpdateUser(String userId, String name, String address, String phone) {
		String query = "UPDATE users SET name = ?, address= ?, phone_number= ? WHERE user_id = ?";
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, name);
			stmt.setString(2, address);
			stmt.setString(3, phone);
			stmt.setString(4, userId);
			int res = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public User getUser(String userId) throws SQLException, UserNotFoundException {
		String query = "SELECT * FROM users WHERE user_id = ?";
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, Integer.parseInt(userId));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new User(
						rs.getString("user_id"),
						rs.getString("name"),
						rs.getString("address"),
						rs.getString("phone_number")); 
			} else {
				throw new UserNotFoundException("User not found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public long getLastUserId() throws SQLException {
		String query = "SELECT MAX(user_id) FROM users";
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public boolean Login(String uid, String passwd) {
		String query = "SELECT * FROM users where user_id ='" + uid + "' and passwd = '" + passwd + "'";
		// System.out.println(query);
		try {
			Connection conn = MySQLConnection.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}
}
