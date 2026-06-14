package com.expense.sse;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * Central SSE hub.
 *
 * Every browser tab that opens /sse/stream registers an AsyncContext here.
 * Any part of the app can call SSEManager.broadcast(event, data)
 * to push a message to ALL connected clients instantly.
 *
 * Event types:
 *   bill_due      – bill reminder alert
 *   overdue       – overdue bill
 *   auto_expense  – auto-expense was added
 *   heartbeat     – keep-alive ping every 25 s
 */
public class SSEManager {

    private static final Logger LOG = Logger.getLogger(SSEManager.class.getName());

    // clientId → AsyncContext
    private static final ConcurrentHashMap<String, AsyncContext> CLIENTS
        = new ConcurrentHashMap<>();

    private static final AtomicLong ID_GEN = new AtomicLong(0);

    // Scheduled heartbeat executor
    private static ScheduledExecutorService heartbeatExec;

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    public static void init() {
        heartbeatExec = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "SSE-Heartbeat");
            t.setDaemon(true);
            return t;
        });
        // Send a heartbeat comment every 25 s to prevent proxy/browser timeout
        heartbeatExec.scheduleAtFixedRate(() -> {
            broadcastRaw(": heartbeat\n\n");
        }, 25, 25, TimeUnit.SECONDS);
        LOG.info("[SSEManager] Initialized. Heartbeat every 25s.");
    }

    public static void shutdown() {
        if (heartbeatExec != null) heartbeatExec.shutdownNow();
        CLIENTS.clear();
    }

    // ── Register / Unregister ─────────────────────────────────────────────────

    public static String register(AsyncContext ctx) {
        String id = "client-" + ID_GEN.incrementAndGet();
        CLIENTS.put(id, ctx);
        LOG.info("[SSEManager] Client connected: " + id + " | Total: " + CLIENTS.size());

        // Send connected confirmation
        send(ctx, "connected", "{\"message\":\"SSE connected\",\"clientId\":\"" + id + "\"}");
        return id;
    }

    public static void unregister(String id) {
        CLIENTS.remove(id);
        LOG.info("[SSEManager] Client disconnected: " + id + " | Total: " + CLIENTS.size());
    }

    public static int clientCount() { return CLIENTS.size(); }

    // ── Broadcast to ALL clients ───────────────────────────────────────────────

    /**
     * Broadcast a named SSE event with JSON data to every connected browser.
     *
     * @param event  SSE event name  (e.g. "bill_due", "overdue")
     * @param json   JSON string     (e.g. "{\"title\":\"EB Bill\",\"amount\":\"1200\"}")
     */
    public static void broadcast(String event, String json) {
        if (CLIENTS.isEmpty()) return;

        String payload = buildSSEMessage(event, json);
        CLIENTS.forEach((id, ctx) -> {
            try {
                PrintWriter w = ctx.getResponse().getWriter();
                w.write(payload);
                w.flush();
                if (w.checkError()) {
                    CLIENTS.remove(id);
                    LOG.info("[SSEManager] Removed broken client: " + id);
                }
            } catch (Exception e) {
                CLIENTS.remove(id);
            }
        });
    }

    /** Convenience: build and broadcast a BillAlert */
    public static void broadcastBillAlert(String type, String title, String amount,
                                           long daysLeft, String dueDate, String category) {
        String json = String.format(
            "{\"type\":\"%s\",\"title\":\"%s\",\"amount\":\"%s\"," +
            "\"daysLeft\":%d,\"dueDate\":\"%s\",\"category\":\"%s\"}",
            escape(type), escape(title), escape(amount),
            daysLeft, escape(dueDate), escape(category));

        String event = daysLeft < 0 ? "overdue"
                     : daysLeft == 0 ? "due_today"
                     : "bill_due";
        broadcast(event, json);
    }

    /** Broadcast auto-expense added event */
    public static void broadcastAutoExpense(String title, String amount) {
        String json = String.format(
            "{\"title\":\"%s\",\"amount\":\"%s\",\"message\":\"Auto-expense added\"}",
            escape(title), escape(amount));
        broadcast("auto_expense", json);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private static void send(AsyncContext ctx, String event, String json) {
        try {
            PrintWriter w = ctx.getResponse().getWriter();
            w.write(buildSSEMessage(event, json));
            w.flush();
        } catch (IOException ignored) {}
    }

    private static void broadcastRaw(String raw) {
        CLIENTS.forEach((id, ctx) -> {
            try {
                PrintWriter w = ctx.getResponse().getWriter();
                w.write(raw);
                w.flush();
                if (w.checkError()) CLIENTS.remove(id);
            } catch (Exception e) {
                CLIENTS.remove(id);
            }
        });
    }

    /**
     * SSE wire format:
     *   event: bill_due\n
     *   data: {"title":"EB Bill"}\n
     *   \n
     */
    private static String buildSSEMessage(String event, String data) {
        return "event: " + event + "\ndata: " + data + "\n\n";
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace("\"", "\\\"").replace("\n", " ");
    }
}
