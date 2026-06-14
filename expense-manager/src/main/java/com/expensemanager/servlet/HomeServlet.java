package com.expensemanager.servlet;

import com.expensemanager.dao.CategoryDAO;
import com.expensemanager.dao.ColumnDefinitionDAO;
import com.expensemanager.dao.SubCategoryDAO;
import com.expensemanager.dao.TransactionDAO;
import com.expensemanager.model.Transaction;
import com.expensemanager.model.TransactionFilter;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(TransactionServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession();
//		log.debug("Session Timeout: {}", session.getMaxInactiveInterval());

		int bookId = (Integer) req.getSession().getAttribute("activeBookId");
		TransactionServlet ts=new TransactionServlet();
		TransactionFilter filter = ts.parseFilter(req, bookId);
//		System.out.println("Book ID: "+bookId);
		try {
			TransactionDAO txnDAO = new TransactionDAO();
			CategoryDAO catDAO = new CategoryDAO();
			ColumnDefinitionDAO colDAO = new ColumnDefinitionDAO();
			SubCategoryDAO scDAO = new SubCategoryDAO();

			List<Transaction> txns = txnDAO.findByFilter(filter);
			int total = txnDAO.countByFilter(filter);
			int totalPages = (int) Math.ceil((double) total / filter.getPageSize());

			req.setAttribute("transactions", txns);
			req.setAttribute("incomeCategories", catDAO.findByType("INCOME"));
			req.setAttribute("expenseCategories", catDAO.findByType("EXPENSE"));
			req.setAttribute("incomeColumns", colDAO.findByType("INCOME"));
			req.setAttribute("expenseColumns", colDAO.findByType("EXPENSE"));
			req.setAttribute("subCategories", scDAO.findAll());
			req.setAttribute("filter", filter);
			req.setAttribute("page", filter.getPage());
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("total", total);
			
			BigDecimal income = txnDAO.sumByType("INCOME", bookId);
			BigDecimal expense = txnDAO.sumByType("EXPENSE", bookId);
			List<Transaction> recent = txnDAO.findAll(null, 1, 5, bookId);

			req.setAttribute("totalIncome", income);
			req.setAttribute("totalExpense", expense);
			req.setAttribute("balance", income.subtract(expense));
			req.setAttribute("recentTxns", recent);
		} catch (Exception e) {
			req.setAttribute("dbError", e.getMessage());
		}
		req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    // Delegate to TransactionServlet
		log.debug("HomeServlet debug");
	    req.getRequestDispatcher("/transactions").forward(req, resp);
	}
}
