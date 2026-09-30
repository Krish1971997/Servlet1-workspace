package com.expensemanager.servlet;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.expensemanager.dao.RecycleBinDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Ported from Android RecycleBin screen — lists soft-deleted records for
 * the active book and restores / purges them.
 */
@WebServlet("/recycle")
public class RecycleBinServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(RecycleBinServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer bookId = (Integer) req.getSession().getAttribute("activeBookId");
		try {
			req.setAttribute("items", new RecycleBinDAO().findAll(bookId));
		} catch (Exception e) {
			log.error("RecycleBinServlet doGet error: {}", e.getMessage(), e);
			req.setAttribute("dbError", e.getMessage());
		}
		req.getRequestDispatcher("/WEB-INF/views/recycle.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		long binId = 0;
		try {
			binId = Long.parseLong(req.getParameter("id"));
		} catch (Exception ignored) {
		}

		try {
			RecycleBinDAO dao = new RecycleBinDAO();
			if ("restore".equals(action)) {
				dao.restore(binId);
			} else if ("purge".equals(action)) {
				dao.purge(binId);
			}
		} catch (Exception e) {
			log.error("RecycleBinServlet doPost error: {}", e.getMessage(), e);
		}
		resp.sendRedirect(req.getContextPath() + "/recycle?msg=" + ("restore".equals(action) ? "restored" : "purged"));
	}
}
