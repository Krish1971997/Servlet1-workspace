package test;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/* ----------------------
   ServletContextListener: runs at app startup/shutdown
   ---------------------- */
@WebListener
public class AppContextListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("[ContextListener] contextInitialized() -> application starting");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("[ContextListener] contextDestroyed() -> application stopping");
	}
}
