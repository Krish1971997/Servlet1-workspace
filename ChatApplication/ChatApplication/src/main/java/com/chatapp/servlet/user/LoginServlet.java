package com.chatapp.servlet.user;

import com.chatapp.dao.UserDAO;
import com.chatapp.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private final UserDAO userDAO = new UserDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/jsp/common/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String identifier = req.getParameter("identifier"); // username or email
		String password = req.getParameter("password");

		System.out.println("username : " + identifier + " - pass : " + password);

		User user = userDAO.authenticate(identifier, password);

		if (user == null) {
			req.setAttribute("error", "Invalid credentials.");
			req.getRequestDispatcher("/jsp/common/login.jsp").forward(req, resp);
			return;
		}

		if (!user.isActive()) {
			req.setAttribute("error", "User " + user.getUsername() + " is inactive...");
			req.getRequestDispatcher("/jsp/common/login.jsp").forward(req, resp);
			return;
		}

		if (user.isAdmin()) {
//            Regular login page → redirect admins to admin panel
//            req.setAttribute("error", "Please use the admin login page.");
//            req.getRequestDispatcher("/jsp/common/login.jsp").forward(req, resp);
//            return;
			HttpSession session = req.getSession(true);
			session.setAttribute("loggedUser", user);
			session.setMaxInactiveInterval(3600);
			resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
		} else {
			HttpSession session = req.getSession(true);
			session.setAttribute("loggedUser", user);
			session.setMaxInactiveInterval(3600);
			resp.sendRedirect(req.getContextPath() + "/feed");
		}

	}
}
