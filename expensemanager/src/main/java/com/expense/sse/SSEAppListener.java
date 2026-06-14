package com.expense.sse;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Starts SSEManager when the app boots, shuts it down when app stops.
 * Separate from BackupScheduler and ReminderScheduler so concerns stay isolated.
 */
@WebListener
public class SSEAppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        SSEManager.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        SSEManager.shutdown();
    }
}
