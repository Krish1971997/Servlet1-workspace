
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h1>Hello, Servlet World!</h1>");

		// Access context attribute set by listener
		long startTime = (Long) getServletContext().getAttribute("appStartTime");
		out.println("<p>App started at: " + new java.util.Date(startTime) + "</p>");

		out.close();
	}
}