package com.expense.sse;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Browser connects to GET /sse/stream
 * Response is text/event-stream — kept open forever.
 * Server pushes events via SSEManager.broadcast().
 *
 * Must be async-supported: @WebServlet(asyncSupported = true)
 */
@WebServlet(urlPatterns = "/sse/stream", asyncSupported = true)
public class SSEServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(SSEServlet.class.getName());
    private static final long   TIMEOUT_MS = 5 * 60 * 1000; // 5 minutes, browser reconnects

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // SSE headers
        res.setContentType("text/event-stream");
        res.setCharacterEncoding("UTF-8");
        res.setHeader("Cache-Control", "no-cache");
        res.setHeader("Connection",    "keep-alive");
        res.setHeader("X-Accel-Buffering", "no"); // disable Nginx buffering

        // Start async
        AsyncContext ctx = req.startAsync();
        ctx.setTimeout(TIMEOUT_MS);

        String clientId = SSEManager.register(ctx);

        ctx.addListener(new AsyncListener() {
            @Override public void onComplete(AsyncEvent e)  { SSEManager.unregister(clientId); }
            @Override public void onTimeout(AsyncEvent e)   { SSEManager.unregister(clientId); ctx.complete(); }
            @Override public void onError(AsyncEvent e)     { SSEManager.unregister(clientId); }
            @Override public void onStartAsync(AsyncEvent e){}
        });

        LOG.fine("[SSEServlet] Client registered: " + clientId);
    }
}
