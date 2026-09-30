package com.expensemanager.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.expensemanager.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Ported from Android InsightsActivity — month-over-month category
 * comparison, top-spender highlight cards and proportional category bars
 * for the active book.
 */
@WebServlet("/insights")
public class InsightsServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(InsightsServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer bookId = (Integer) req.getSession().getAttribute("activeBookId");
		if (bookId == null) {
			resp.sendRedirect(req.getContextPath() + "/books");
			return;
		}

		YearMonth ym = YearMonth.now();
		try {
			if (req.getParameter("year") != null && req.getParameter("month") != null)
				ym = YearMonth.of(Integer.parseInt(req.getParameter("year")), Integer.parseInt(req.getParameter("month")));
		} catch (Exception ignored) {
		}
		String type = "INCOME".equalsIgnoreCase(req.getParameter("type")) ? "INCOME" : "EXPENSE";

		List<Map<String, Object>> cur = breakdown(bookId, type, ym);
		List<Map<String, Object>> prev = breakdown(bookId, type, ym.minusMonths(1));
		BigDecimal curTotal = sum(cur);
		BigDecimal prevTotal = sum(prev);

		int pctChange = prevTotal.compareTo(BigDecimal.ZERO) == 0 ? 0
				: curTotal.subtract(prevTotal).multiply(BigDecimal.valueOf(100))
						.divide(prevTotal, 0, RoundingMode.HALF_UP).intValue();

		// Biggest increase vs last month (Android insight sentence).
		Map<String, BigDecimal> prevMap = new LinkedHashMap<>();
		for (Map<String, Object> r : prev)
			prevMap.put((String) r.get("name"), (BigDecimal) r.get("total"));
		String worstName = null;
		BigDecimal worstDelta = BigDecimal.ZERO;
		for (Map<String, Object> r : cur) {
			BigDecimal d = ((BigDecimal) r.get("total"))
					.subtract(prevMap.getOrDefault((String) r.get("name"), BigDecimal.ZERO));
			if (d.compareTo(worstDelta) > 0) {
				worstDelta = d;
				worstName = (String) r.get("name");
			}
		}

		StringBuilder ins = new StringBuilder();
		if ("EXPENSE".equals(type)) {
			if (pctChange > 0)
				ins.append("Spending is up ").append(pctChange).append("% vs last month. ");
			else if (pctChange < 0)
				ins.append("Good job \u2014 spending is down ").append(Math.abs(pctChange)).append("% vs last month. ");
			else
				ins.append("Spending is flat vs last month. ");
			if (worstName != null && worstDelta.compareTo(BigDecimal.ZERO) > 0)
				ins.append("Biggest jump: ").append(worstName).append(" (+")
						.append(worstDelta.setScale(0, RoundingMode.HALF_UP)).append(").");
			else if (!cur.isEmpty())
				ins.append("Top category: ").append(cur.get(0).get("name")).append(".");
		} else {
			ins.append("Income for the month is \u20B9").append(curTotal.setScale(0, RoundingMode.HALF_UP)).append(".");
		}

		for (Map<String, Object> r : cur) {
			BigDecimal amt = (BigDecimal) r.get("total");
			r.put("pct", curTotal.compareTo(BigDecimal.ZERO) == 0 ? 0
					: amt.multiply(BigDecimal.valueOf(100)).divide(curTotal, 0, RoundingMode.HALF_UP).intValue());
		}

		YearMonth prevNav = ym.minusMonths(1);
		YearMonth nextNav = ym.plusMonths(1);
		req.setAttribute("year", ym.getYear());
		req.setAttribute("month", ym.getMonthValue());
		req.setAttribute("monthLabel", ym.getMonth().name() + " " + ym.getYear());
		req.setAttribute("prevNav", "year=" + prevNav.getYear() + "&month=" + prevNav.getMonthValue());
		req.setAttribute("nextNav", "year=" + nextNav.getYear() + "&month=" + nextNav.getMonthValue());
		req.setAttribute("type", type);
		req.setAttribute("curTotal", curTotal);
		req.setAttribute("prevTotal", prevTotal);
		req.setAttribute("prevMonthLabel", prevNav.getMonth().name() + " " + prevNav.getYear());
		req.setAttribute("pctChange", pctChange);
		req.setAttribute("insight", ins.toString());
		req.setAttribute("cats", cur);

		req.getRequestDispatcher("/WEB-INF/views/insights.jsp").forward(req, resp);
	}

	private List<Map<String, Object>> breakdown(Integer bookId, String type, YearMonth ym) {
		List<Map<String, Object>> rows = new ArrayList<>();
		if (bookId == null)
			return rows;
		String sql = """
				SELECT c.id, c.name, COALESCE(SUM(t.amount), 0) AS total
				FROM transactions t
				JOIN categories c ON t.category_id = c.id
				WHERE t.book_id = ? AND t.type = ?::txn_type
				  AND t.txn_datetime >= ? AND t.txn_datetime < ?
				GROUP BY c.id, c.name
				ORDER BY total DESC
				""";
		try (Connection conn = DBConnection.getInstance().getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, bookId);
			ps.setString(2, type);
			ps.setTimestamp(3, Timestamp.valueOf(ym.atDay(1).atStartOfDay()));
			ps.setTimestamp(4, Timestamp.valueOf(ym.plusMonths(1).atDay(1).atStartOfDay()));
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> m = new LinkedHashMap<>();
					m.put("id", rs.getInt(1));
					m.put("name", rs.getString(2));
					m.put("total", rs.getBigDecimal(3));
					rows.add(m);
				}
			}
		} catch (Exception e) {
			log.error("InsightsServlet breakdown error: {}", e.getMessage(), e);
		}
		return rows;
	}

	private BigDecimal sum(List<Map<String, Object>> rows) {
		BigDecimal t = BigDecimal.ZERO;
		for (Map<String, Object> r : rows)
			t = t.add((BigDecimal) r.get("total"));
		return t;
	}
}
