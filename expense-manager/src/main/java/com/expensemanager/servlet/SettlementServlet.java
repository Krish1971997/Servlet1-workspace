package com.expensemanager.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.expensemanager.dao.SettlementLinkDAO;
import com.expensemanager.dao.TransactionDAO;
import com.expensemanager.model.SettlementLink;
import com.expensemanager.model.Transaction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Ported from Android SettlementLinkActivity — link a settlement
 * transaction to the transactions it settles, with partial amounts, and
 * cancel individual links. Cross-book links are allowed.
 */
@WebServlet("/settlement")
public class SettlementServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(SettlementServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idStr = req.getParameter("txnId");
		if (idStr == null) {
			resp.sendRedirect(req.getContextPath() + "/transactions");
			return;
		}
		try {
			int txnId = Integer.parseInt(idStr);
			TransactionDAO txnDAO = new TransactionDAO();
			SettlementLinkDAO linkDAO = new SettlementLinkDAO();

			Transaction txn = txnDAO.findById(txnId);
			if (txn == null) {
				resp.sendRedirect(req.getContextPath() + "/transactions?error=notfound");
				return;
			}

			List<Map<String, Object>> linkRows = new ArrayList<>();
			for (SettlementLink l : linkDAO.findForTransaction(txnId)) {
				Map<String, Object> m = new LinkedHashMap<>();
				m.put("id", l.getId());
				m.put("amount", l.getAmount());
				int other = l.otherSide(txnId);
				m.put("otherId", other);
				Transaction otherT = txnDAO.findById(other);
				m.put("otherLabel", otherT != null
						? ("#" + other + " &middot; " + otherT.getFormattedDate() + " &middot; &#8377;" + otherT.getAmount()
								+ (otherT.getNote() != null && !otherT.getNote().isBlank() ? " &middot; " + otherT.getNote() : ""))
						: ("#" + other));
				linkRows.add(m);
			}

			// Candidate transactions to link against: same book (links may
			// still target other books from the detail page), not this txn,
			// and not already linked in any direction.
			List<Transaction> candidates = txnDAO.findUnlinkedCandidates(txnId, txn.getBookId());

			req.setAttribute("txn", txn);
			req.setAttribute("linkRows", linkRows);
			req.setAttribute("candidates", candidates);
			req.setAttribute("linkedTotal", linkDAO.sumLinkedFor(txnId));
		} catch (Exception e) {
			log.error("SettlementServlet doGet error: {}", e.getMessage(), e);
			req.setAttribute("dbError", e.getMessage());
		}
		req.getRequestDispatcher("/WEB-INF/views/settlement.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		String txnIdStr = req.getParameter("txnId");
		int txnId = 0;
		try {
			txnId = Integer.parseInt(txnIdStr);
		} catch (Exception ignored) {
		}
		SettlementLinkDAO dao = new SettlementLinkDAO();

		try {
			if ("link".equals(action)) {
				int linkedTxnId = Integer.parseInt(req.getParameter("linkedTxnId"));
				BigDecimal amount = new BigDecimal(req.getParameter("amount").trim());
				dao.insert(txnId, linkedTxnId, amount);
			} else if ("unlink".equals(action) || "cancel".equals(action)) {
				long linkId = Long.parseLong(req.getParameter("linkId"));
				dao.delete(linkId);
			}
		} catch (Exception e) {
			log.error("SettlementServlet doPost error: {}", e.getMessage(), e);
			resp.sendRedirect(req.getContextPath() + "/settlement?txnId=" + txnId + "&error=failed");
			return;
		}
		resp.sendRedirect(req.getContextPath() + "/settlement?txnId=" + txnId + "&msg=done");
	}
}
