package com.auction.servlet;

import com.auction.model.User;
import com.auction.util.DatabaseUtil;
import com.auction.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/jsp/login")
public class LoginServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		try (Connection conn = DatabaseUtil.getConnection()) {
			String sql = "SELECT * FROM users WHERE email = ? and password = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
//                String hashedPassword = rs.getString("password");
//                if (PasswordUtil.checkPassword(password, hashedPassword)) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setRole(rs.getString("role"));

				HttpSession session = request.getSession();
				session.setAttribute("user", user);

				if ("ADMIN".equalsIgnoreCase(user.getRole())) {
					response.sendRedirect("admin_dashboard.jsp");
				} else {
					response.sendRedirect("/jsp/user_dashboard.jsp");
				}
				return;
//                }
			}
			request.setAttribute("error", "Invalid email or password");
			request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "Server error");
			request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
		}
	}
}
