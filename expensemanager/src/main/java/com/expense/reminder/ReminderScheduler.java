package com.expense.reminder;

import com.expense.dao.BillReminderDAO;
import com.expense.dao.TransactionDAO;
import com.expense.model.BillReminder;
import com.expense.model.Transaction;
import com.expense.sse.SSEManager;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Runs every 60 seconds (tight loop for real-time feel).
 * For each due/overdue bill:
 *   1. Logs notification to DB (once per day per bill)
 *   2. Broadcasts SSE event → browser gets it INSTANTLY
 *   3. Auto-adds expense + advances date if autoAddExpense=true
 */
@WebListener
public class ReminderScheduler implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(ReminderScheduler.class.getName());
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            new BillReminderDAO().createTablesIfNotExist();
            LOG.info("[ReminderScheduler] Tables ready.");
        } catch (Exception e) {
            LOG.severe("[ReminderScheduler] Table init failed: " + e.getMessage());
        }

        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "ReminderScheduler");
            t.setDaemon(true);
            return t;
        });

        // Check every 60 seconds — fast enough for real-time feel
        scheduler.scheduleAtFixedRate(this::checkReminders, 5, 60, TimeUnit.SECONDS);
        LOG.info("[ReminderScheduler] Started — checking every 60 seconds.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) scheduler.shutdownNow();
    }

    // ── Core check ────────────────────────────────────────────────────────────

    private void checkReminders() {
        try {
            BillReminderDAO dao     = new BillReminderDAO();
            List<BillReminder> due  = dao.getDueSoon();

            for (BillReminder b : due) {

                // Only notify once per day per bill
                if (dao.alreadyNotifiedToday(b.getId())) continue;

                long daysLeft = b.getDaysUntilDue();

                // 1. Log to DB
                dao.logNotification(b.getId(), (int) daysLeft);

                // 2. SSE push → real-time to all browsers
                SSEManager.broadcastBillAlert(
                    b.isOverdue() ? "overdue" : b.isDueToday() ? "due_today" : "bill_due",
                    b.getTitle(),
                    b.getAmount().toPlainString(),
                    daysLeft,
                    b.getNextDueDate().toString(),
                    b.getCategory()
                );

                // 3. Auto-add expense if configured and due/overdue
                if (b.isAutoAddExpense() && daysLeft <= 0) {
                    autoAddExpense(b);
                    dao.advanceDueDate(b.getId(), b.computeNextDate());

                    // Push auto-expense notification
                    SSEManager.broadcastAutoExpense(b.getTitle(),
                        b.getAmount().toPlainString());

                    LOG.info("[ReminderScheduler] Auto-expense done + date advanced: " + b.getTitle());
                }

                LOG.info("[ReminderScheduler] SSE pushed: " + b.getTitle()
                         + " | daysLeft=" + daysLeft);
            }
        } catch (Exception e) {
            LOG.severe("[ReminderScheduler] Error: " + e.getMessage());
        }
    }

    private void autoAddExpense(BillReminder b) {
        try {
            Transaction t = new Transaction();
            t.setTransactionDate(Date.valueOf(LocalDate.now()));
            t.setTransactionTime(Time.valueOf(LocalTime.now()));
            t.setAmount(b.getAmount());
            t.setCategory(b.getCategory());
            t.setNote("Auto: " + b.getTitle());
            t.setType("expense");
            new TransactionDAO().addTransaction(t);
        } catch (Exception e) {
            LOG.warning("[ReminderScheduler] Auto-expense failed: " + e.getMessage());
        }
    }
}
