package com.expensemanager.servlet;

import com.expensemanager.dao.TransactionDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/reports")
public class ReportServlet extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            TransactionDAO dao = new TransactionDAO();

            var income  = dao.sumByType("INCOME");
            var expense = dao.sumByType("EXPENSE");
            List<Map<String, Object>> monthly  = dao.monthlyTrend(6);
            List<Map<String, Object>> expByCat = dao.expenseByCategory();
            List<Map<String, Object>> incByCat = dao.incomeByCategory();

            req.setAttribute("totalIncome",  income);
            req.setAttribute("totalExpense", expense);
            req.setAttribute("balance",      income.subtract(expense));

            req.setAttribute("monthlyJson",  mapper.writeValueAsString(monthly));
            req.setAttribute("expCatJson",   mapper.writeValueAsString(expByCat));
            req.setAttribute("incCatJson",   mapper.writeValueAsString(incByCat));
        } catch (Exception e) {
            req.setAttribute("dbError", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/reports.jsp").forward(req, resp);
    }
}
