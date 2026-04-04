package com.chatapp.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/logout")
public class AdminLogoutServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session != null)
			session.invalidate();
		resp.sendRedirect(req.getContextPath() + "/login");
//        resp.sendRedirect(req.getContextPath() + "/admin/login");
	}
}
