package com.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/realtime", asyncSupported = true)
public class RealTimeDataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Queue to store clients waiting for updates
    private final Queue<AsyncContext> clients = new ConcurrentLinkedQueue<>();
    
    // Simulated data source (could be replaced with actual data source)
    private final DataGenerator dataGenerator = new DataGenerator();
    
    // Thread pool for processing clients
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Start the data generation thread
        new Thread(() -> {
            while (true) {
                try {
                    // Generate new data every second
                    Thread.sleep(1000);
                    String newData = dataGenerator.generateData();
                    // Notify all clients about new data
                    notifyClients(newData);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Set response headers for SSE
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        
        // Create async context
        final AsyncContext asyncContext = request.startAsync();
        // No timeout for long polling
        asyncContext.setTimeout(0);
        
        // Add this client to our connected clients
        clients.add(asyncContext);
        
        // Handle client disconnect
        asyncContext.addListener(new AsyncListenerAdapter() {
            @Override
            public void onComplete(AsyncEvent event) {
                clients.remove(asyncContext);
            }
            
            @Override
            public void onTimeout(AsyncEvent event) {
                clients.remove(asyncContext);
                asyncContext.complete();
            }
        });
    }
    
    private void notifyClients(String data) {
        // Iterate through all connected clients
        for (AsyncContext asyncContext : clients) {
            executor.execute(() -> {
                try {
                    PrintWriter writer = asyncContext.getResponse().getWriter();
                    writer.write("data: " + data + "\n\n");
                    writer.flush();
                    // Don't complete the context as we want to keep the connection open
                } catch (IOException e) {
                    clients.remove(asyncContext);
                    asyncContext.complete();
                }
            });
        }
    }
    
    @Override
    public void destroy() {
        executor.shutdown();
        super.destroy();
    }
}

// DataGenerator.java - Simulated data source
class DataGenerator {
    private int count = 0;
    
    public String generateData() {
        count++;
        return "{\"timestamp\": " + System.currentTimeMillis() + 
               ", \"value\": " + (Math.random() * 100) + 
               ", \"count\": " + count + "}";
    }
}