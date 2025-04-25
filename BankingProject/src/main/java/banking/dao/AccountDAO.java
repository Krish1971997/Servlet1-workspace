package banking.dao;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import banking.models.User;
import banking.exceptions.UserNotFoundException;
import banking.models.Account;
import banking.models.CheckingsAccount;
import banking.models.SavingsAccount;
import banking.utils.MySQLConnection;
import banking.interfaces.AccountManager;

public class AccountDAO implements AccountManager {

	public Account getAccountById(String accountId) throws SQLException, UserNotFoundException {
		String query = "SELECT a.account_id, a.user_id, a.type, u.name, u.address, u.phone_number " +
				"FROM accounts a " +
				"JOIN users u ON a.user_id = u.user_id " +
				"WHERE a.account_id = ?";
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, accountId);
			try {
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					String userId = rs.getString("user_id");
					String name = rs.getString("name");
					String address = rs.getString("address");
					String phoneNumber = rs.getString("phone_number");
					User user = new User(userId, name, address, phoneNumber);

					String type = rs.getString("type");
					if (type.equals("savings")) {
						return this.getSavingsAccount(accountId, userId);
					} else if (type.equals("checkings")) {
						return this.getCheckingsAccount(accountId, userId);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int deleteSavingsAccount(String aid) {
		String query = "DELETE FROM  savings_accounts WHERE account_id = ?";
		String accountQuery = "DELETE FROM accounts WHERE account_id = ?";
		int res = 0;
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, aid);
			res = stmt.executeUpdate();
			PreparedStatement stmtt = conn.prepareStatement(accountQuery);
			stmtt.setString(1, aid);
			res = stmtt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public int deleteCheckingsAccount(String aid) {
		String query = "DELETE FROM  checkings_accounts WHERE account_id = ?";
		String accountQuery = "DELETE FROM accounts WHERE account_id = ?";
		int res = 0;
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, aid);
			res = stmt.executeUpdate();
			PreparedStatement stmtt = conn.prepareStatement(accountQuery);
			stmtt.setString(1, aid);
			res = stmtt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public void UpdateRate(String rate, String aid) {
		String query = "UPDATE savings_accounts SET rate = ? WHERE account_id = ?";
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, rate);
			stmt.setString(2, aid);
			int res = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void UpdateOverDraft(String od, String aid) {
		String query = "UPDATE checkings_accounts SET over_draft_limit = ? WHERE account_id = ?";
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, od);
			stmt.setString(2, aid);
			int res = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<SavingsAccount> getAllSavingsAccount(boolean everified) {

		if (everified == false){
			return null;
		}
		String query = "SELECT a.account_id, a.user_id, a.type, u.name, u.address, u.phone_number,s.balance,s.rate FROM accounts a JOIN users u ON a.user_id = u.user_id JOIN savings_accounts s ON s.account_id = a.account_id WHERE a.type='savings'";
		List<SavingsAccount> list = new ArrayList<>();
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				User user = new User(rs.getString("user_id"), rs.getString("name"), rs.getString("address"),
						rs.getString("phone_number"));
				SavingsAccount account = new SavingsAccount(rs.getString("account_id"), user, rs.getDouble("balance"),
						rs.getDouble("rate"));
				list.add(account);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<CheckingsAccount> getAllCheckingsAccount() {
		String query = "SELECT a.account_id, a.user_id, a.type, u.name, u.address, u.phone_number,c.balance,c.over_draft_limit FROM accounts a JOIN users u ON a.user_id = u.user_id JOIN checkings_accounts c ON c.account_id = a.account_id WHERE a.type='checkings'";
		List<CheckingsAccount> list = new ArrayList<>();
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				User user = new User(rs.getString("user_id"), rs.getString("name"), rs.getString("address"),
						rs.getString("phone_number"));
				CheckingsAccount account = new CheckingsAccount(rs.getString("account_id"), user,
						rs.getDouble("balance"), rs.getDouble("over_draft_limit"));
				list.add(account);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public String getAccountHolderUserId(String accountId) {
		String query = "SELECT user_id FROM accounts WHERE account_id = ?";
		String UserId = "";
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, accountId);
			try {
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					UserId = rs.getString("user_id");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return UserId;
	}

	public String getTpin(String accountId) {
		String query = "SELECT transaction_pin FROM accounts WHERE account_id = ?";
		String res = "";
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, accountId);
			try {
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					res = rs.getString("transaction_pin");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public String getMD5(String pin) throws SQLException {
		String query = "SELECT MD5( ? ) AS pin";
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, pin);
			try {
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					return rs.getString("pin");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int createSavingsAccount(SavingsAccount account, String tpin) throws SQLException {
		String insertAccountSQL = "INSERT INTO accounts (account_id, user_id, type, transaction_pin) VALUES (?, ?, ?, MD5(?))";
		String insertSavingsSQL = "INSERT INTO savings_accounts (account_id, balance, rate) VALUES (?, ?, ?)";
		int res = 0;
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement Accountstmt = conn.prepareStatement(insertAccountSQL);
			PreparedStatement Savingstmt = conn.prepareStatement(insertSavingsSQL);
			conn.setAutoCommit(false);
			Accountstmt.setString(1, account.getAccountId());
			Accountstmt.setString(2, account.getAccountHolder().getUserId());
			Accountstmt.setString(3, "savings");
			Accountstmt.setString(4, tpin);
			Accountstmt.executeUpdate();
			Savingstmt.setString(1, account.getAccountId());
			Savingstmt.setDouble(2, account.getBalance());
			Savingstmt.setDouble(3, account.getRate());
			res = Savingstmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public int createCheckingsAccount(CheckingsAccount account, String tpin) throws SQLException {
		String insertAccountSQL = "INSERT INTO accounts (account_id, user_id, type, transaction_pin) VALUES (?, ?, ?, MD5(?))";
		String insertCheckingsSQL = "INSERT INTO checkings_accounts (account_id, balance, over_draft_limit) VALUES (?, ?, ?)";
		int res = 0;
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement Accountstmt = conn.prepareStatement(insertAccountSQL);
			PreparedStatement stmtCheckings = conn.prepareStatement(insertCheckingsSQL);
			conn.setAutoCommit(false);
			Accountstmt.setString(1, account.getAccountId());
			Accountstmt.setString(2, account.getAccountHolder().getUserId());
			Accountstmt.setString(3, "checkings");
			Accountstmt.setString(4, tpin);
			Accountstmt.executeUpdate();
			stmtCheckings.setString(1, account.getAccountId());
			stmtCheckings.setDouble(2, account.getBalance());
			stmtCheckings.setDouble(3, account.getOverDraftLimit());
			res = stmtCheckings.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public SavingsAccount getSavingsAccount(String accountId, String userId)
			throws SQLException, UserNotFoundException {
		String query = "SELECT * FROM savings_accounts WHERE account_id = ?";
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, accountId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				UserDAO userDAO = new UserDAO();
				User accountHolder = userDAO.getUser(userId);
				return new SavingsAccount(rs.getString("account_id"), accountHolder, rs.getDouble("balance"),
						rs.getDouble("rate"));
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public CheckingsAccount getCheckingsAccount(String accountId, String userId)
			throws SQLException, UserNotFoundException {
		String query = "SELECT * FROM checkings_accounts WHERE account_id = ?";
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, accountId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				UserDAO userDAO = new UserDAO();
				User accountHolder = userDAO.getUser(userId);
				return new CheckingsAccount(rs.getString("account_id"), accountHolder, rs.getDouble("balance"),
						rs.getDouble("over_draft_limit"));
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public long getLastAccountId() throws SQLException {
		String query = "SELECT MAX(account_id) FROM accounts";
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void updateAccountBalance(Account account) throws SQLException {
		String query = "";
		if (account instanceof SavingsAccount) {
			query = "UPDATE savings_accounts SET balance = ? WHERE account_id = ?";
		} else if (account instanceof CheckingsAccount) {
			query = "UPDATE checkings_accounts SET balance = ? WHERE account_id = ?";
		}

		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setDouble(1, account.getBalance());
			stmt.setString(2, account.getAccountId());
			System.out.println(stmt.executeUpdate());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
