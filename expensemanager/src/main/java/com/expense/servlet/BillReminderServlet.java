package com.expense.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import com.expense.dao.BillReminderDAO;
import com.expense.dao.TransactionDAO;
import com.expense.model.BillReminder;
import com.expense.model.BillReminder.Frequency;
import com.expense.model.Transaction;
import com.expense.sse.SSEManager;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/reminders/*")
public class BillReminderServlet extends HttpServlet {

	private final BillReminderDAO dao = new BillReminderDAO();
	private final TransactionDAO txDao = new TransactionDAO();
	private final Gson gson = new Gson();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String path = req.getPathInfo();
		if (path == null || path.equals("/"))
			path = "/list";

		switch (path) {
		case "/list":
			showList(req, res);
			break;
		case "/add":
			showForm(req, res, null);
			break;
		case "/edit":
			try {
				showForm(req, res, dao.getById(intParam(req, "id")));
			} catch (Exception e) {
				error(req, res, e.getMessage());
				res.sendRedirect(req.getContextPath() + "/reminders/list");
			}
			break;
		case "/delete":
			try {
				dao.delete(intParam(req, "id"));
				success(req, res, "Reminder deleted.");
			} catch (Exception e) {
				error(req, res, e.getMessage());
			}
			res.sendRedirect(req.getContextPath() + "/reminders/list");
			break;
		case "/paid":
			handlePaid(req, res);
			break;
		case "/toggle":
			handleToggle(req, res);
			break;
		default:
			showList(req, res);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		try {
			BillReminder b = buildFromRequest(req);
			if (b.getId() == 0) {
				dao.save(b);
				success(req, res, "✅ Reminder created!");
			} else {
				dao.update(b);
				success(req, res, "✅ Reminder updated!");
			}
		} catch (Exception e) {
			error(req, res, "Save failed: " + e.getMessage());
		}
		res.sendRedirect(req.getContextPath() + "/reminders/list");
	}

	// ── Handlers ──────────────────────────────────────────────────────────────

	private void showList(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			req.setAttribute("reminders", dao.getAll(false));
			req.setAttribute("dueSoon", dao.getDueSoon());
			req.setAttribute("recentAlerts", dao.getRecentNotifications(15));
			req.setAttribute("sseCount", SSEManager.clientCount());
			req.setAttribute("activePage", "reminders");
			req.setAttribute("pageTitle", "Bill Reminders");
			req.getRequestDispatcher("/WEB-INF/jsp/reminders.jsp").forward(req, res);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void showForm(HttpServletRequest req, HttpServletResponse res, BillReminder b)
			throws ServletException, IOException {
		req.setAttribute("reminder", b);
		req.setAttribute("frequencies", Frequency.values());
		req.setAttribute("activePage", "reminders");
		req.getRequestDispatcher("/WEB-INF/jsp/reminder-form.jsp").forward(req, res);
	}

	private void handlePaid(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			int id = intParam(req, "id");
			BillReminder b = dao.getById(id);
			if (b == null) {
				error(req, res, "Reminder not found.");
				res.sendRedirect(req.getContextPath() + "/reminders/list");
				return;
			}

			// Add expense
			Transaction t = new Transaction();
			t.setTransactionDate(Date.valueOf(LocalDate.now()));
			t.setTransactionTime(Time.valueOf(LocalTime.now()));
			t.setAmount(b.getAmount());
			t.setCategory(b.getCategory());
			t.setNote("Paid: " + b.getTitle());
			t.setType("expense");
			txDao.addTransaction(t);

			// SSE push — notify other tabs instantly
			SSEManager.broadcast("paid", "{\"title\":\"" + b.getTitle() + "\"," + "\"amount\":\""
					+ b.getAmount().toPlainString() + "\"," + "\"message\":\"Bill marked as paid\"}");

			// Advance or deactivate
			if (b.getFrequency() == Frequency.ONCE) {
				b.setActive(false);
				dao.update(b);
			} else {
				dao.advanceDueDate(id, b.computeNextDate());
			}

			success(req, res, "✅ " + b.getTitle() + " paid — expense added & next date updated.");
		} catch (Exception e) {
			error(req, res, "Error: " + e.getMessage());
		}
		res.sendRedirect(req.getContextPath() + "/reminders/list");
	}

	private void handleToggle(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			int id = intParam(req, "id");
			BillReminder b = dao.getById(id);
			if (b != null) {
				b.setActive(!b.isActive());
				dao.update(b);
			}
		} catch (Exception e) {
			error(req, res, e.getMessage());
		}
		res.sendRedirect(req.getContextPath() + "/reminders/list");
	}

	// ── Helpers ───────────────────────────────────────────────────────────────

	private BillReminder buildFromRequest(HttpServletRequest req) throws Exception {
		BillReminder b = new BillReminder();
		String idStr = req.getParameter("id");
		if (idStr != null && !idStr.isEmpty())
			b.setId(Integer.parseInt(idStr));
		b.setTitle(req.getParameter("title"));
		b.setAmount(new BigDecimal(req.getParameter("amount")));
		b.setCategory(req.getParameter("category"));
		b.setFrequency(Frequency.valueOf(req.getParameter("frequency")));
		b.setNextDueDate(Date.valueOf(req.getParameter("nextDueDate")));
		b.setRemindDaysBefore(Integer.parseInt(req.getParameter("remindDaysBefore")));
		b.setAutoAddExpense("on".equals(req.getParameter("autoAddExpense")));
		b.setNote(req.getParameter("note"));
		b.setActive(true);
		return b;
	}

	private int intParam(HttpServletRequest req, String name) {
		return Integer.parseInt(req.getParameter(name));
	}

	private void success(HttpServletRequest req, HttpServletResponse res, String msg) {
		req.getSession().setAttribute("successMsg", msg);
	}

	private void error(HttpServletRequest req, HttpServletResponse res, String msg) {
		req.getSession().setAttribute("errorMsg", msg);
	}
}
