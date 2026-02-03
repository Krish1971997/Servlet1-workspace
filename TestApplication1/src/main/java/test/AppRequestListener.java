package test;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppRequestListener implements ServletRequestListener {
	@Override
	public void requestInitialized(ServletRequestEvent sre) {
// create a StringBuilder to trace request-level execution order
		StringBuilder trace = new StringBuilder();
		trace.append("requestInitialized -> ");
		sre.getServletRequest().setAttribute("trace", trace);
		System.out.println("[RequestListener] requestInitialized()");
	}

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		System.out.println("[RequestListener] requestDestroyed()");
		Object t = sre.getServletRequest().getAttribute("trace");
		if (t != null) {
// requestDestroyed runs after response is committed in many containers; we log to console
			System.out.println("[RequestListener] final request trace (console): " + t.toString());
		}
	}
}