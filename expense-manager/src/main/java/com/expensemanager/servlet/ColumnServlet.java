package com.expensemanager.servlet;

import com.expensemanager.dao.ColumnDefinitionDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;

@WebServlet("/columns")
public class ColumnServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String colName = req.getParameter("colName");
        String type    = req.getParameter("type");   // INCOME | EXPENSE

        if (colName != null && !colName.isBlank() && type != null) {
            try {
                new ColumnDefinitionDAO().insert(colName.trim(), type);
            } catch (Exception e) {
                // ignore duplicate
            }
        }
        resp.sendRedirect(req.getContextPath() + "/transactions?success=col&filter=" + type);
    }
}
