package com.expensemanager.servlet;

import com.expensemanager.dao.CategoryDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;

@WebServlet("/categories")
public class CategoryServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");
        String type = req.getParameter("type");   // INCOME | EXPENSE
        String back = req.getParameter("back");   // redirect target

        if (name != null && !name.isBlank() && type != null) {
            try {
                new CategoryDAO().insert(name.trim(), type);
            } catch (Exception e) {
                // ignore duplicate
            }
        }
        String redirect = (back != null && !back.isBlank()) ? back : "/transactions";
        resp.sendRedirect(req.getContextPath() + redirect + "?success=cat");
    }
}
