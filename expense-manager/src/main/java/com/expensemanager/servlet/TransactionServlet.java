package com.expensemanager.servlet;

import com.expensemanager.dao.CategoryDAO;
import com.expensemanager.dao.ColumnDefinitionDAO;
import com.expensemanager.dao.TransactionDAO;
import com.expensemanager.model.Transaction;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet("/transactions")
public class TransactionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String filter   = req.getParameter("filter");   // INCOME | EXPENSE | null
        String pageStr  = req.getParameter("page");
        int page        = (pageStr != null) ? Integer.parseInt(pageStr) : 1;
        int pageSize    = 15;

        try {
            TransactionDAO txnDao = new TransactionDAO();
            CategoryDAO catDao    = new CategoryDAO();
            ColumnDefinitionDAO colDao = new ColumnDefinitionDAO();

            List<Transaction> txns = txnDao.findAll(filter, page, pageSize);
            int total = txnDao.count(filter);
            int totalPages = (int) Math.ceil((double) total / pageSize);

            req.setAttribute("transactions",   txns);
            req.setAttribute("incomeCategories",  catDao.findByType("INCOME"));
            req.setAttribute("expenseCategories", catDao.findByType("EXPENSE"));
            req.setAttribute("incomeColumns",  colDao.findByType("INCOME"));
            req.setAttribute("expenseColumns", colDao.findByType("EXPENSE"));
            req.setAttribute("filter",     filter);
            req.setAttribute("page",       page);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("total",      total);
        } catch (Exception e) {
            req.setAttribute("dbError", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/transactions.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String typeStr    = req.getParameter("type");
        String amountStr  = req.getParameter("amount");
        String catIdStr   = req.getParameter("categoryId");
        String note       = req.getParameter("note");
        String dateStr    = req.getParameter("dateTime");

        if (typeStr == null || amountStr == null || catIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/transactions?error=missing");
            return;
        }

        Transaction t = new Transaction();
        t.setType(Transaction.Type.valueOf(typeStr.toUpperCase()));
        t.setAmount(new BigDecimal(amountStr));
        t.setCategoryId(Integer.parseInt(catIdStr));
        t.setNote(note);
        t.setDateTime(dateStr != null && !dateStr.isBlank()
                ? LocalDateTime.parse(dateStr) : LocalDateTime.now());

        // Collect custom field values (param name = "custom_<col_key>")
        Map<String, String> customs = new LinkedHashMap<>();
        req.getParameterNames().asIterator().forEachRemaining(p -> {
            if (p.startsWith("custom_")) {
                String key = p.substring(7);
                String val = req.getParameter(p);
                if (val != null && !val.isBlank()) customs.put(key, val);
            }
        });
        t.setCustomValues(customs);

        try {
            new TransactionDAO().insert(t);
            resp.sendRedirect(req.getContextPath() + "/transactions?success=1&filter=" + typeStr);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/transactions?error=" + e.getMessage());
        }
    }
}
