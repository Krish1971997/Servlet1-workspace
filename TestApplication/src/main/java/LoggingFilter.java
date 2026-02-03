import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(urlPatterns = "/*") // Applies to all URLs
public class LoggingFilter implements Filter {

	public void init(FilterConfig config) throws ServletException {
		// Initialization code (e.g., load config)
		System.out.println("LoggingFilter initialized.");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		long startTime = System.currentTimeMillis();

		// Log before servlet processing
		System.out.println("Request URI: " + httpRequest.getRequestURI() + " - Start time: " + startTime);

		// Pass to the next filter or servlet
		chain.doFilter(request, response);
		
		long endTime = System.currentTimeMillis();
		// Log after servlet processing
		System.out.println("Request URI: " + httpRequest.getRequestURI() + " - End time: " + endTime
				+ " - Processing time: " + (endTime - startTime) + " ms");
	}

	public void destroy() {
		// Cleanup code
		System.out.println("LoggingFilter destroyed.");
	}
}