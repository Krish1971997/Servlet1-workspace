package com.expensemanager.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.expensemanager.dao.BudgetTemplateDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Ported from Android BudgetConfigActivity — a shared budget-allocation
 * template (per-category flat amounts) that can be saved once and applied
 * to any book/month. Existing per-category budget rows are never
 * overwritten when applying.
 */
@WebServlet("/budget-template")
public class BudgetTemplateServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(BudgetTemplateServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer bookId = (Integer) req.getSession().getAttribute("activeBookId");
		if (bookId == null) {
			resp.sendRedirect(req.getContextPath() + "/books");
			return;
		}

		int year = parseInt(req.getParameter("year"), java.time.LocalDate.now().getYear());
		int month = parseInt(req.getParameter("month"), java.time.LocalDate.now().getMonthValue());
		String action = req.getParameter("action");
		String qs = "year=" + year + "&month=" + month;

		try {
			BudgetTemplateDAO dao = new BudgetTemplateDAO();

			if ("applyTemplate".equals(action)) {
				int added = dao.applyToBook(bookId, year, month);
				resp.sendRedirect(req.getContextPath() + "/budget?" + qs
						+ (added > 0 ? "&success=templateApplied&added=" + added : "&success=templateNothing"));
				return;
			}

			if ("saveTemplate".equals(action)) {
				String[] catIds = req.getParameterValues("templateCatId");
				String[] amounts = req.getParameterValues("templateAmt");
				Map<Integer, BigDecimal> map = new LinkedHashMap<>();
				if (catIds != null && amounts != null) {
					for (int i = 0; i < catIds.length; i++) {
						String amtStr = i < amounts.length ? amounts[i] : "";
						if (amtStr == null || amtStr.isBlank())
							continue;
						try {
							BigDecimal amt = new BigDecimal(amtStr.trim());
							map.put(Integer.parseInt(catIds[i]), amt);
						} catch (Exception ignored) {
						}
					}
				}
				dao.saveGlobalTemplate(map);
				resp.sendRedirect(req.getContextPath() + "/budget?" + qs + "&success=templateSaved");
				return;
			}

		} catch (Exception e) {
			log.error("BudgetTemplateServlet error: {}", e.getMessage(), e);
			resp.sendRedirect(req.getContextPath() + "/budget?" + qs + "&error=templateFailed");
			return;
		}

		resp.sendRedirect(req.getContextPath() + "/budget?" + qs);
	}

	private int parseInt(String s, int def) {
		try {
			return (s != null && !s.isBlank()) ? Integer.parseInt(s) : def;
		} catch (Exception e) {
			return def;
		}
	}
}
