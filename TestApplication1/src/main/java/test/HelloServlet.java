package test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = "/demo/hello", loadOnStartup = 0)
public class HelloServlet extends HttpServlet {

	@Override
	public void init() throws ServletException {
		super.init();
		System.out.println("[Servlet] init() -> servlet instance created (only once)");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("[Servlet] doGet() called");

// append to request trace
		Object t = req.getAttribute("trace");
		if (t instanceof StringBuilder) {
			((StringBuilder) t).append("servlet-doGet -> ");
		}

// optionally create session to demonstrate session listener
		HttpSession session = req.getSession(true);

		resp.setContentType("text/plain; charset=utf-8");
		PrintWriter out = resp.getWriter();

// write the request-level trace to HTTP response
		Object traceObj = req.getAttribute("trace");
		if (traceObj instanceof StringBuilder) {
			out.println("Request execution trace (in-order):");
			out.println(((StringBuilder) traceObj).toString());
		} else {
			out.println("No trace available");
		}

		out.println();
		out.println("Check server console for lifecycle logs (init/destroy/context/request/session events).");
	}

	@Override
	public void destroy() {
		System.out.println("[Servlet] destroy() -> servlet is being removed (cleanup)");
		super.destroy();
	}
}