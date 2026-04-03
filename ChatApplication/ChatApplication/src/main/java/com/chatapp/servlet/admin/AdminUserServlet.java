package com.chatapp.servlet.admin;

import com.chatapp.dao.UserDAO;
import com.chatapp.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Admin user management.
 * GET  /admin/users      → list all users
 * POST /admin/users?action=deactivate&userId=N
 * POST /admin/users?action=activate&userId=N
 */
@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("users", userDAO.findAllUsers());
        req.getRequestDispatcher("/jsp/admin/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        int userId = Integer.parseInt(req.getParameter("userId"));

        if ("deactivate".equals(action)) userDAO.setActive(userId, false);
        else if ("activate".equals(action)) userDAO.setActive(userId, true);

        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }
}
