package com.chatapp.servlet.admin;

import com.chatapp.dao.UserDAO;
import com.chatapp.model.User;
import com.chatapp.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/login")
public class AdminLoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/admin/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String identifier = req.getParameter("identifier");
        String password   = req.getParameter("password");

        User user = userDAO.authenticate(identifier, password);

        if (user == null || !user.isAdmin()) {
            req.setAttribute("error", "Invalid admin credentials.");
            req.getRequestDispatcher("/jsp/admin/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("loggedUser", user);
        session.setMaxInactiveInterval(3600);
        resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
    }
}
