package com.expense.dao;

import com.expense.model.BillReminder;
import com.expense.model.BillReminder.Frequency;
import com.expense.util.DBConnection;

import java.sql.*;
import java.util.*;

public class BillReminderDAO {

    // ── DDL ───────────────────────────────────────────────────────────────────
    public void createTablesIfNotExist() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS bill_reminders (
                id               SERIAL PRIMARY KEY,
                title            VARCHAR(150)  NOT NULL,
                amount           DECIMAL(15,2) NOT NULL,
                category         VARCHAR(100)  NOT NULL DEFAULT 'Bills',
                frequency        VARCHAR(20)   NOT NULL DEFAULT 'MONTHLY',
                next_due_date    DATE          NOT NULL,
                remind_days_before INT         NOT NULL DEFAULT 3,
                auto_add_expense BOOLEAN       NOT NULL DEFAULT FALSE,
                note             TEXT,
                is_active        BOOLEAN       NOT NULL DEFAULT TRUE,
                created_at       TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
            );
            CREATE TABLE IF NOT EXISTS reminder_notifications (
                id          SERIAL PRIMARY KEY,
                reminder_id INT  NOT NULL REFERENCES bill_reminders(id) ON DELETE CASCADE,
                sent_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                days_before INT NOT NULL,
                status      VARCHAR(20) DEFAULT 'SENT'
            );
            CREATE INDEX IF NOT EXISTS idx_bill_next_due ON bill_reminders(next_due_date);
            CREATE INDEX IF NOT EXISTS idx_bill_active   ON bill_reminders(is_active);
            """;
        try (Connection con = DBConnection.getConnection();
             Statement  st  = con.createStatement()) {
            st.execute(sql);
        }
    }

    // ── INSERT ────────────────────────────────────────────────────────────────
    public int save(BillReminder b) throws SQLException {
        String sql = """
            INSERT INTO bill_reminders
              (title,amount,category,frequency,next_due_date,
               remind_days_before,auto_add_expense,note,is_active)
            VALUES (?,?,?,?,?,?,?,?,?) RETURNING id
            """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString (1, b.getTitle());
            ps.setBigDecimal(2, b.getAmount());
            ps.setString (3, b.getCategory());
            ps.setString (4, b.getFrequency().name());
            ps.setDate   (5, b.getNextDueDate());
            ps.setInt    (6, b.getRemindDaysBefore());
            ps.setBoolean(7, b.isAutoAddExpense());
            ps.setString (8, b.getNote());
            ps.setBoolean(9, b.isActive());
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    public boolean update(BillReminder b) throws SQLException {
        String sql = """
            UPDATE bill_reminders
               SET title=?,amount=?,category=?,frequency=?,next_due_date=?,
                   remind_days_before=?,auto_add_expense=?,note=?,is_active=?
             WHERE id=?
            """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString (1, b.getTitle());
            ps.setBigDecimal(2, b.getAmount());
            ps.setString (3, b.getCategory());
            ps.setString (4, b.getFrequency().name());
            ps.setDate   (5, b.getNextDueDate());
            ps.setInt    (6, b.getRemindDaysBefore());
            ps.setBoolean(7, b.isAutoAddExpense());
            ps.setString (8, b.getNote());
            ps.setBoolean(9, b.isActive());
            ps.setInt    (10, b.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // ── ADVANCE next_due_date after marking paid ───────────────────────────────
    public void advanceDueDate(int id, java.sql.Date newDate) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "UPDATE bill_reminders SET next_due_date=? WHERE id=?")) {
            ps.setDate(1, newDate);
            ps.setInt (2, id);
            ps.executeUpdate();
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    public void delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "DELETE FROM bill_reminders WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ── GET ALL ───────────────────────────────────────────────────────────────
    public List<BillReminder> getAll(boolean activeOnly) throws SQLException {
        String sql = "SELECT * FROM bill_reminders"
                   + (activeOnly ? " WHERE is_active=true" : "")
                   + " ORDER BY next_due_date ASC";
        List<BillReminder> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement  st  = con.createStatement();
             ResultSet  rs  = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // ── GET DUE SOON (for scheduler) ──────────────────────────────────────────
    /** Returns reminders where today >= next_due_date - remind_days_before */
    public List<BillReminder> getDueSoon() throws SQLException {
        String sql = """
            SELECT * FROM bill_reminders
             WHERE is_active = true
               AND next_due_date <= CURRENT_DATE + remind_days_before
               AND next_due_date >= CURRENT_DATE - 1
             ORDER BY next_due_date ASC
            """;
        List<BillReminder> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement  st  = con.createStatement();
             ResultSet  rs  = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public BillReminder getById(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT * FROM bill_reminders WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? mapRow(rs) : null;
        }
    }

    // ── NOTIFICATION LOG ──────────────────────────────────────────────────────
    public void logNotification(int reminderId, int daysBefore) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "INSERT INTO reminder_notifications (reminder_id,days_before) VALUES (?,?)")) {
            ps.setInt(1, reminderId);
            ps.setInt(2, daysBefore);
            ps.executeUpdate();
        }
    }

    /** Check if already notified today for same reminder */
    public boolean alreadyNotifiedToday(int reminderId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT 1 FROM reminder_notifications " +
                 "WHERE reminder_id=? AND DATE(sent_at)=CURRENT_DATE LIMIT 1")) {
            ps.setInt(1, reminderId);
            return ps.executeQuery().next();
        }
    }

    public List<Map<String,Object>> getRecentNotifications(int limit) throws SQLException {
        String sql = """
            SELECT rn.*, br.title, br.amount, br.next_due_date
              FROM reminder_notifications rn
              JOIN bill_reminders br ON br.id = rn.reminder_id
             ORDER BY rn.sent_at DESC
             LIMIT ?
            """;
        List<Map<String,Object>> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String,Object> row = new LinkedHashMap<>();
                row.put("id",           rs.getInt      ("id"));
                row.put("title",        rs.getString   ("title"));
                row.put("amount",       rs.getBigDecimal("amount"));
                row.put("nextDueDate",  rs.getDate     ("next_due_date"));
                row.put("sentAt",       rs.getTimestamp("sent_at"));
                row.put("daysBefore",   rs.getInt      ("days_before"));
                row.put("status",       rs.getString   ("status"));
                list.add(row);
            }
        }
        return list;
    }

    // ── PRIVATE ───────────────────────────────────────────────────────────────
    private BillReminder mapRow(ResultSet rs) throws SQLException {
        BillReminder b = new BillReminder();
        b.setId              (rs.getInt      ("id"));
        b.setTitle           (rs.getString   ("title"));
        b.setAmount          (rs.getBigDecimal("amount"));
        b.setCategory        (rs.getString   ("category"));
        b.setNextDueDate     (rs.getDate     ("next_due_date"));
        b.setRemindDaysBefore(rs.getInt      ("remind_days_before"));
        b.setAutoAddExpense  (rs.getBoolean  ("auto_add_expense"));
        b.setNote            (rs.getString   ("note"));
        b.setActive          (rs.getBoolean  ("is_active"));
        try { b.setFrequency(Frequency.valueOf(rs.getString("frequency"))); }
        catch (Exception e) { b.setFrequency(Frequency.MONTHLY); }
        return b;
    }
}
