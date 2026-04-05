package com.chatapp.servlet.admin;

import com.chatapp.dao.UserDAO;
import com.chatapp.model.User;
import com.chatapp.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/create")
public class AdminSignupServlet extends HttpServlet {

	private final UserDAO userDAO = new UserDAO();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String username = trim(req.getParameter("username"));
		String email = trim(req.getParameter("email"));
		String password = req.getParameter("password");
		String role = req.getParameter("role").toLowerCase();
		String confirm = req.getParameter("confirmPassword");

		String error=ValidationUtil.isSignUpValid(username, email, password, confirm, role);
		if (error != null) {
			error(req, resp, error);
			return;
		}

		String err = userDAO.register(username, email, password, role);
		if (err != null) {
			error(req, resp, err);
			return;
		}

		List<User> users = userDAO.findAllUsers();
		req.setAttribute("success", "account created!");
		req.setAttribute("users", users);
		req.getRequestDispatcher("/jsp/admin/users.jsp").forward(req, resp);
	}

	private void error(HttpServletRequest req, HttpServletResponse resp, String msg)
			throws ServletException, IOException {
		req.setAttribute("error", msg);
		List<User> users = userDAO.findAllUsers();
		req.setAttribute("users", users);
		req.getRequestDispatcher("/jsp/admin/users.jsp").forward(req, resp);
	}

	private String trim(String s) {
		return s == null ? "" : s.trim();
	}
}
