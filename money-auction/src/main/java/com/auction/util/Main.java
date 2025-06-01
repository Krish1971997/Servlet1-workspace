package com.auction.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpSession;

import com.auction.model.User;

public class Main {

	public static void main(String[] args) {

		String email = "wfkrishna@gmail.com";
		String password = "Krishna@123";
		try (Connection conn = DatabaseUtil.getConnection()) {
			System.out.println(conn);
			String sql = "SELECT * FROM users WHERE email = ? and password= ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			System.out.println(rs);
			if (rs.next()) {
				System.out.println("inside if condition");
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setRole(rs.getString("role"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
