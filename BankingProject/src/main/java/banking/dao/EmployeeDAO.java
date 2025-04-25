package banking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import banking.exceptions.UserNotFoundException;
import banking.interfaces.EmployeeManager;
import banking.utils.MySQLConnection;

public class EmployeeDAO implements EmployeeManager {
	public String getPassword(String empid) {
		String query = "SELECT passwd FROM employee where employee_id = ?";
		String res = "";
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, empid);
			try {
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					res = rs.getString("passwd");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public String getEmployeeName(String empid) {
		String query = "SELECT employee_name FROM employee";
		try {
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getString("employee_name");
			} else {
				throw new Exception("Failed to get Name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

}
