package com.chatapp.servlet.user;

import com.chatapp.dao.UserDAO;
import com.chatapp.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

	private final UserDAO userDAO = new UserDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/jsp/common/signup.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String username = trim(req.getParameter("username"));
		String email = trim(req.getParameter("email"));
		String password = req.getParameter("password");
		String confirm = req.getParameter("confirmPassword");

		// ── Validation ─────────────────────────────────────────
		if (!ValidationUtil.isValidUsername(username)) {
			error(req, resp, "Username must be 3-50 chars (letters, digits, _).");
			return;
		}
		if (!ValidationUtil.isValidEmail(email)) {
			error(req, resp, "Invalid email address.");
			return;
		}
		if (!ValidationUtil.isValidPassword(password)) {
			error(req, resp, "Password must be at least 6 characters.");
			return;
		}
		if (!password.equals(confirm)) {
			error(req, resp, "Passwords do not match.");
			return;
		}

		String err = userDAO.register(username, email, password, "user");
		if (err != null) {
			error(req, resp, err);
			return;
		}

		req.setAttribute("success", "Account created! Please log in.");
		req.getRequestDispatcher("/jsp/common/login.jsp").forward(req, resp);
	}

	private void error(HttpServletRequest req, HttpServletResponse resp, String msg)
			throws ServletException, IOException {
		req.setAttribute("error", msg);
		req.getRequestDispatcher("/jsp/common/signup.jsp").forward(req, resp);
	}

	private String trim(String s) {
		return s == null ? "" : s.trim();
	}
}
