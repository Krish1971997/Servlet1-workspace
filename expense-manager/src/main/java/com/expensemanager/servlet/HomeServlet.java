package com.expensemanager.servlet;

import com.expensemanager.dao.TransactionDAO;
import com.expensemanager.model.Transaction;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            TransactionDAO dao = new TransactionDAO();
            BigDecimal income  = dao.sumByType("INCOME");
            BigDecimal expense = dao.sumByType("EXPENSE");
            List<Transaction> recent = dao.findAll(null, 1, 5);

            req.setAttribute("totalIncome",  income);
            req.setAttribute("totalExpense", expense);
            req.setAttribute("balance",      income.subtract(expense));
            req.setAttribute("recentTxns",   recent);
        } catch (Exception e) {
            req.setAttribute("dbError", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }
}
