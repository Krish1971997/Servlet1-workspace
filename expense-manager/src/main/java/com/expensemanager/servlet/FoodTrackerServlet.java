package com.expensemanager.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.expensemanager.dao.TransactionDAO;
import com.expensemanager.model.Transaction;
import com.expensemanager.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Ported from Android FoodTrackerActivity — daily Breakfast / Lunch / Dinner
 * expense grid for the current month's auto-created "&lt;Month&gt; &lt;Year&gt; Food"
 * cashbook. Category "Food" and its Breakfast/Lunch/Dinner sub-categories
 * are COMMON (book_id IS NULL), shared across books. Fixed transaction
 * times per meal: Breakfast 9:00 AM, Lunch 1:00 PM, Dinner 8:30 PM, and
 * payment type is always "Cash". Saving upserts the matching transaction
 * per cell (create if none exists for that date+meal, update the amount if
 * one does, soft-delete it if cleared back to 0).
 */
@WebServlet("/foodtracker")
public class FoodTrackerServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(FoodTrackerServlet.class);

	private static final Map<String, LocalTime> MEAL_TIMES = new LinkedHashMap<>();

	static {
		MEAL_TIMES.put("Breakfast", LocalTime.of(9, 0));
		MEAL_TIMES.put("Lunch", LocalTime.of(13, 0));
		MEAL_TIMES.put("Dinner", LocalTime.of(20, 30));
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer sessionBook = (Integer) req.getSession().getAttribute("activeBookId");
		if (sessionBook == null) {
			resp.sendRedirect(req.getContextPath() + "/books");
			return;
		}

		YearMonth ym = YearMonth.now();
		try {
			if (req.getParameter("ym") != null && !req.getParameter("ym").isBlank())
				ym = YearMonth.parse(req.getParameter("ym"));
		} catch (Exception ignored) {
		}

		try {
			int[] ids = ensureFoodSetup(ym); // {bookId, categoryId, breakfastId, lunchId, dinnerId}
			int bookId = ids[0];

			Map<String, Map<String, BigDecimal>> grid = loadGrid(bookId, ids[1], ym);

			// Days of the month + per-meal totals.
			List<Map<String, Object>> days = new ArrayList<>();
			Map<String, BigDecimal> mealTotals = new LinkedHashMap<>();
			for (String meal : MEAL_TIMES.keySet())
				mealTotals.put(meal, BigDecimal.ZERO);
			BigDecimal monthTotal = BigDecimal.ZERO;

			for (int d = 1; d <= ym.lengthOfMonth(); d++) {
				LocalDate date = ym.atDay(d);
				String ds = date.format(DateTimeFormatter.ISO_DATE);
				Map<String, BigDecimal> dayRow = grid.getOrDefault(ds, new LinkedHashMap<>());
				Map<String, Object> dm = new LinkedHashMap<>();
				dm.put("day", d);
				dm.put("dateStr", ds);
				dm.put("label", date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + d);
				dm.put("meals", dayRow);
				days.add(dm);
				for (String meal : MEAL_TIMES.keySet()) {
					BigDecimal v = dayRow.get(meal);
					if (v != null) {
						mealTotals.put(meal, mealTotals.get(meal).add(v));
						monthTotal = monthTotal.add(v);
					}
				}
			}

			YearMonth prevNav = ym.minusMonths(1);
			YearMonth nextNav = ym.plusMonths(1);
			req.setAttribute("ymParam", ym.toString());
			req.setAttribute("monthLabel", ym.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + ym.getYear());
			req.setAttribute("prevNav", "ym=" + prevNav);
			req.setAttribute("nextNav", "ym=" + nextNav);
			req.setAttribute("days", days);
			req.setAttribute("meals", MEAL_TIMES.keySet());
			req.setAttribute("mealTotals", mealTotals);
			req.setAttribute("monthTotal", monthTotal);
		} catch (Exception e) {
			log.error("FoodTrackerServlet doGet error: {}", e.getMessage(), e);
			req.setAttribute("dbError", e.getMessage());
		}
		req.getRequestDispatcher("/WEB-INF/views/foodtracker.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		YearMonth ym = YearMonth.now();
		try {
			if (req.getParameter("ym") != null && !req.getParameter("ym").isBlank())
				ym = YearMonth.parse(req.getParameter("ym"));
		} catch (Exception ignored) {
		}

		try {
			String action = req.getParameter("action");
			int[] ids = ensureFoodSetup(ym);
			int bookId = ids[0], catId = ids[1];
			int subId;
			if ("Breakfast".equals(req.getParameter("meal")))
				subId = ids[2];
			else if ("Lunch".equals(req.getParameter("meal")))
				subId = ids[3];
			else
				subId = ids[4];
			String meal = req.getParameter("meal");

			if ("save".equals(action)) {
				LocalDate date = LocalDate.parse(req.getParameter("date"));
				BigDecimal amount;
				try {
					String s = req.getParameter("amount");
					amount = (s == null || s.isBlank()) ? BigDecimal.ZERO : new BigDecimal(s.trim());
				} catch (Exception e) {
					amount = BigDecimal.ZERO;
				}

				TransactionDAO txnDAO = new TransactionDAO();
				Transaction existing = findCellTxn(bookId, catId, subId, date);

				if (amount.compareTo(BigDecimal.ZERO) > 0) {
					if (existing != null) {
						Transaction updated = new Transaction();
						updated.setId(existing.getId());
						updated.setType(existing.getType());
						updated.setBookId(existing.getBookId());
						updated.setAmount(amount);
						updated.setDateTime(existing.getDateTime());
						updated.setCategoryId(catId);
						updated.setCategoryName(existing.getCategoryName());
						updated.setSubcategoryid(subId);
						updated.setSubCategoryName(existing.getSubCategoryName());
						updated.setNote(existing.getNote());
						updated.setPaymentType(existing.getPaymentType() != null ? existing.getPaymentType() : "Cash");
						txnDAO.update(existing, updated);
					} else {
						Transaction t = new Transaction();
						t.setType(Transaction.Type.EXPENSE);
						t.setDateTime(LocalDateTime.of(date, MEAL_TIMES.get(meal)));
						t.setAmount(amount);
						t.setCategoryId(catId);
						t.setSubcategoryid(subId);
						t.setBookId(bookId);
						t.setNote(meal);
						t.setPaymentType("Cash");
						txnDAO.insert(t);
					}
				} else if (existing != null) {
					txnDAO.delete(existing.getId()); // cleared to 0 → soft-delete
				}
			}
		} catch (Exception e) {
			log.error("FoodTrackerServlet doPost error: {}", e.getMessage(), e);
		}
		resp.sendRedirect(req.getContextPath() + "/foodtracker?ym=" + ym);
	}

	/** {bookId, categoryId, breakfastId, lunchId, dinnerId} — auto-creates what's missing. */
	private int[] ensureFoodSetup(YearMonth ym) throws Exception {
		String bookName = ym.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + ym.getYear() + " Food";
		DBConnection db = DBConnection.getInstance();
		Connection conn = db.getConnection();
		try {
			int bookId = -1;
			try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM cash_books WHERE name = ?")) {
				ps.setString(1, bookName);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next())
						bookId = rs.getInt(1);
				}
			}
			if (bookId < 0) {
				try (PreparedStatement ps = conn.prepareStatement(
						"INSERT INTO cash_books (name, description) VALUES (?, 'Auto-created by Food Tracker') RETURNING id")) {
					ps.setString(1, bookName);
					try (ResultSet rs = ps.executeQuery()) {
						if (rs.next())
							bookId = rs.getInt(1);
					}
				}
			}

			int catId = -1;
			try (PreparedStatement ps = conn.prepareStatement(
					"SELECT id FROM categories WHERE name = 'Food' AND type = 'EXPENSE' AND book_id IS NULL")) {
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next())
						catId = rs.getInt(1);
				}
			}
			if (catId < 0) {
				try (PreparedStatement ps = conn.prepareStatement(
						"INSERT INTO categories (name, type, book_id) VALUES ('Food', 'EXPENSE'::txn_type, NULL) RETURNING id")) {
					try (ResultSet rs = ps.executeQuery()) {
						if (rs.next())
							catId = rs.getInt(1);
					}
				}
			}

			int[] subIds = new int[3];
			int i = 0;
			for (String meal : MEAL_TIMES.keySet()) {
				int subId = -1;
				try (PreparedStatement ps = conn.prepareStatement(
						"SELECT sub_categories_id FROM sub_categories WHERE name = ? AND category_id = ?")) {
					ps.setString(1, meal);
					ps.setInt(2, catId);
					try (ResultSet rs = ps.executeQuery()) {
						if (rs.next())
							subId = rs.getInt(1);
					}
				}
				if (subId < 0) {
					try (PreparedStatement ps = conn.prepareStatement(
							"INSERT INTO sub_categories (name, category_id) VALUES (?, ?) RETURNING sub_categories_id")) {
						ps.setString(1, meal);
						ps.setInt(2, catId);
						try (ResultSet rs = ps.executeQuery()) {
							if (rs.next())
								subId = rs.getInt(1);
						}
					}
				}
				subIds[i++] = subId;
			}
			return new int[] { bookId, catId, subIds[0], subIds[1], subIds[2] };
		} finally {
			db.releaseConnection(conn);
		}
	}

	private Map<String, Map<String, BigDecimal>> loadGrid(int bookId, int catId, YearMonth ym) throws Exception {
		Map<String, Map<String, BigDecimal>> grid = new LinkedHashMap<>();
		String sql = """
				SELECT to_char(t.txn_datetime, 'YYYY-MM-DD') AS d, sc.name AS meal, SUM(t.amount) AS total
				FROM transactions t
				JOIN sub_categories sc ON t.sub_categories_id = sc.sub_categories_id
				WHERE t.book_id = ? AND t.category_id = ?
				  AND t.txn_datetime >= ? AND t.txn_datetime < ?
				GROUP BY d, meal
				""";
		DBConnection db = DBConnection.getInstance();
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, bookId);
			ps.setInt(2, catId);
			ps.setTimestamp(3, Timestamp.valueOf(ym.atDay(1).atStartOfDay()));
			ps.setTimestamp(4, Timestamp.valueOf(ym.plusMonths(1).atDay(1).atStartOfDay()));
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					grid.computeIfAbsent(rs.getString(1), k -> new LinkedHashMap<>())
							.put(rs.getString(2), rs.getBigDecimal(3));
				}
			}
		} finally {
			db.releaseConnection(conn);
		}
		return grid;
	}

	private Transaction findCellTxn(int bookId, int catId, int subId, LocalDate date) throws Exception {
		String sql = """
				SELECT t.id FROM transactions t
				WHERE t.book_id = ? AND t.category_id = ? AND t.sub_categories_id = ?
				  AND t.txn_datetime >= ? AND t.txn_datetime < ?
				ORDER BY t.id LIMIT 1
				""";
		DBConnection db = DBConnection.getInstance();
		Connection conn = db.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, bookId);
			ps.setInt(2, catId);
			ps.setInt(3, subId);
			ps.setTimestamp(4, Timestamp.valueOf(date.atStartOfDay()));
			ps.setTimestamp(5, Timestamp.valueOf(date.plusDays(1).atStartOfDay()));
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return new TransactionDAO().findById(rs.getInt(1));
			}
		} catch (Exception e) {
			log.error("findCellTxn error: {}", e.getMessage(), e);
		} finally {
			db.releaseConnection(conn);
		}
		return null;
	}
}
