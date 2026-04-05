package com.chatapp.servlet.admin;

import com.chatapp.dao.UserDAO;
import com.chatapp.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Admin user management. GET /admin/users → list all users POST
 * /admin/users?action=deactivate&userId=N POST
 * /admin/users?action=activate&userId=N
 */
@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {

	private final UserDAO userDAO = new UserDAO();
	private static final int PAGE_SIZE = 250;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session != null) {
			String flashSuccess = (String) session.getAttribute("flashSuccess");
			String flashError = (String) session.getAttribute("flashError");
			if (flashSuccess != null) {
				req.setAttribute("success", flashSuccess);
				session.removeAttribute("flashSuccess");
			}
			if (flashError != null) {
				req.setAttribute("error", flashError);
				session.removeAttribute("flashError");
			}
		}

		// Pagination
		int page = 1;
		String pageParam = req.getParameter("page");
		if (pageParam != null && !pageParam.isEmpty()) {
			page = Integer.parseInt(pageParam);
		}

		int totalUsers = userDAO.countAllUsers();
		int totalPages = (int) Math.ceil((double) totalUsers / PAGE_SIZE);
		
		System.out.println("totalPages : "+totalPages+" - totalUsers: "+totalUsers);
		System.out.println("page : "+page);
		int count=userDAO.findAllUsersPaginated(page, PAGE_SIZE).size();
		System.out.println("Count : "+count);
		
		req.setAttribute("users", userDAO.findAllUsersPaginated(page, PAGE_SIZE));
		req.setAttribute("currentPage", page);
		req.setAttribute("totalPages", totalPages);
		req.getRequestDispatcher("/jsp/admin/users.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		int userId = Integer.parseInt(req.getParameter("userId"));

		if ("deactivate".equals(action)) {

			HttpSession session = req.getSession(false);
			if (session == null) {
				resp.sendRedirect(req.getContextPath() + "/login");
				return;
			}
			User user = (User) session.getAttribute("loggedUser");
			int uid = user.getId();
			User actionUser = userDAO.findUser(userId);

			if (uid == userId) {
				userDAO.setActive(userId, false);
				session.invalidate();
				resp.sendRedirect(req.getContextPath() + "/login");
				return;
			}
			userDAO.setActive(userId, false);
			req.setAttribute("users", userDAO.findAllUsers());
			req.setAttribute("success", "User " + actionUser.getUsername() + " deactivated");
			req.getRequestDispatcher("/jsp/admin/users.jsp").forward(req, resp);
			return;
		} else if ("activate".equals(action)) {
			userDAO.setActive(userId, true);
			HttpSession session = req.getSession(false);
			User user = (User) session.getAttribute("loggedUser");
			User actionUser = userDAO.findUser(userId);
			req.setAttribute("users", userDAO.findAllUsers());
			req.setAttribute("success", "User " + actionUser.getUsername() + " activated");
			req.getRequestDispatcher("/jsp/admin/users.jsp").forward(req, resp);
		} else if ("delete".equals(action)) {
			HttpSession session = req.getSession(false);
			if (session == null) {
				resp.sendRedirect(req.getContextPath() + "/login");
				return;
			}
			User user = (User) session.getAttribute("loggedUser");
			int uid = user.getId();
			User actionUser = userDAO.findUser(userId);

			if (uid == userId) {
				boolean err = userDAO.DeleteUser(userId);
				if (!err) {
					error(req, resp, "User not deleted");
				}
				session.invalidate();
				resp.sendRedirect(req.getContextPath() + "/login");
				return;
			}
			userDAO.DeleteUser(userId);
			req.setAttribute("users", userDAO.findAllUsers());
			req.setAttribute("success", "User " + actionUser.getUsername() + " deleted");
			req.getRequestDispatcher("/jsp/admin/users.jsp").forward(req, resp);
			return;
		}
	}

	private void error(HttpServletRequest req, HttpServletResponse resp, String msg)
			throws ServletException, IOException {
		req.setAttribute("error", msg);
		List<User> users = userDAO.findAllUsers();
		req.setAttribute("users", users);
		req.getRequestDispatcher("/jsp/admin/users.jsp").forward(req, resp);
	}
}
