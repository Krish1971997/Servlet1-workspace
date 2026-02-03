import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener // Registers the listener automatically
public class AppListener implements ServletContextListener, HttpSessionListener {

	private static int activeSessions = 0;

	// ServletContextListener methods
	public void contextInitialized(ServletContextEvent sce) {
		// Called when the web app starts
		sce.getServletContext().setAttribute("appStartTime", System.currentTimeMillis());
		System.out.println("Application started at: " + new java.util.Date());
	}

	public void contextDestroyed(ServletContextEvent sce) {
		// Called when the web app shuts down
		System.out.println("Application shutting down.");
	}

	// HttpSessionListener methods
	public void sessionCreated(HttpSessionEvent se) {
		activeSessions++;
		System.out.println("Session created. Active sessions: " + activeSessions);
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		activeSessions--;
		System.out.println("Session destroyed. Active sessions: " + activeSessions);
	}
}