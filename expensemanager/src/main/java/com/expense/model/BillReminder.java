package com.expense.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BillReminder {

    public enum Frequency { DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY, ONCE }

    private int        id;
    private String     title;
    private BigDecimal amount;
    private String     category;
    private Frequency  frequency;
    private Date       nextDueDate;
    private int        remindDaysBefore;
    private boolean    autoAddExpense;
    private String     note;
    private boolean    active;

    // ── Computed helpers ─────────────────────────────────────────────────────

    /** Days remaining until due (negative = overdue) */
    public long getDaysUntilDue() {
        return ChronoUnit.DAYS.between(LocalDate.now(),
               nextDueDate.toLocalDate());
    }

    public boolean isOverdue()   { return getDaysUntilDue() < 0; }
    public boolean isDueToday()  { return getDaysUntilDue() == 0; }
    public boolean isDueSoon()   { return getDaysUntilDue() > 0
                                       && getDaysUntilDue() <= remindDaysBefore; }

    /** Badge label shown in UI */
    public String getStatusLabel() {
        long d = getDaysUntilDue();
        if (d < 0)  return "Overdue by " + Math.abs(d) + "d";
        if (d == 0) return "Due Today!";
        if (d <= remindDaysBefore) return "Due in " + d + " day" + (d == 1 ? "" : "s");
        return "Due in " + d + " days";
    }

    /** CSS class for coloring the badge */
    public String getStatusClass() {
        long d = getDaysUntilDue();
        if (d < 0)  return "status-overdue";
        if (d == 0) return "status-today";
        if (d <= remindDaysBefore) return "status-soon";
        return "status-ok";
    }

    /** Advance nextDueDate by one frequency period */
    public Date computeNextDate() {
        LocalDate current = nextDueDate.toLocalDate();
        LocalDate next;
        switch (frequency) {
            case DAILY:       next = current.plusDays(1);    break;
            case WEEKLY:      next = current.plusWeeks(1);   break;
            case MONTHLY:     next = current.plusMonths(1);  break;
            case QUARTERLY:   next = current.plusMonths(3);  break;
            case YEARLY:      next = current.plusYears(1);   break;
            default:          next = current;                break;  // ONCE stays
        }
        return Date.valueOf(next);
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────
    public int getId()                      { return id; }
    public void setId(int v)                { this.id = v; }

    public String getTitle()                { return title; }
    public void setTitle(String v)          { this.title = v; }

    public BigDecimal getAmount()           { return amount; }
    public void setAmount(BigDecimal v)     { this.amount = v; }

    public String getCategory()             { return category; }
    public void setCategory(String v)       { this.category = v; }

    public Frequency getFrequency()         { return frequency; }
    public void setFrequency(Frequency v)   { this.frequency = v; }

    public Date getNextDueDate()            { return nextDueDate; }
    public void setNextDueDate(Date v)      { this.nextDueDate = v; }

    public int getRemindDaysBefore()        { return remindDaysBefore; }
    public void setRemindDaysBefore(int v)  { this.remindDaysBefore = v; }

    public boolean isAutoAddExpense()       { return autoAddExpense; }
    public void setAutoAddExpense(boolean v){ this.autoAddExpense = v; }

    public String getNote()                 { return note; }
    public void setNote(String v)           { this.note = v; }

    public boolean isActive()               { return active; }
    public void setActive(boolean v)        { this.active = v; }
}
