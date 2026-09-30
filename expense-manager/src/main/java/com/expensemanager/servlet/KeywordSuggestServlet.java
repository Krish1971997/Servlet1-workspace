package com.expensemanager.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.expensemanager.dao.KeywordMappingDAO;
import com.expensemanager.model.KeywordMapping;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Ported from Android TransactionEntryActivity — while the user types the
 * note/remark this endpoint returns (a) the keyword → category/sub-category
 * suggestion (💡 chip) and (b) past matching notes for autocomplete.
 */
@WebServlet("/keywords/suggest")
public class KeywordSuggestServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(KeywordSuggestServlet.class);

	private final Gson gson = new Gson();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		String note = req.getParameter("note");
		String type = "INCOME".equalsIgnoreCase(req.getParameter("type")) ? "INCOME" : "EXPENSE";
		Integer bookId = (Integer) req.getSession().getAttribute("activeBookId");

		Map<String, Object> out = new LinkedHashMap<>();
		try {
			KeywordMappingDAO dao = new KeywordMappingDAO();

			if ("1".equals(req.getParameter("notes"))) {
				List<String> notes = dao.suggestNotes(note, bookId);
				out.put("notes", notes != null ? notes : new ArrayList<>());
			} else {
				KeywordMapping m = dao.suggest(note, type, bookId);
				if (m != null) {
					out.put("category", m.getCategoryId());
					out.put("categoryName", m.getCategoryName());
					out.put("subCategory", m.getSubCategoryId() != null ? String.valueOf(m.getSubCategoryId()) : "");
					out.put("subCategoryName", m.getSubCategoryName() != null ? m.getSubCategoryName() : "");
				}
			}
		} catch (Exception e) {
			log.error("KeywordSuggestServlet error: {}", e.getMessage(), e);
		}
		resp.getWriter().write(gson.toJson(out));
	}
}
