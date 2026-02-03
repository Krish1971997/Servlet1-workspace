package test;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = "/demo/*")
public class LoggingFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) {
		System.out.println("[Filter] init()");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Object t = request.getAttribute("trace");
		if (t instanceof StringBuilder) {
			((StringBuilder) t).append("filter-pre -> ");
		}
		System.out.println("[Filter] doFilter() - pre-processing");

// continue the chain (this will call the servlet)
		chain.doFilter(request, response);

// post-processing after servlet returns
		System.out.println("[Filter] doFilter() - post-processing");
		if (t instanceof StringBuilder) {
			((StringBuilder) t).append("filter-post -> ");
		}
	}

	@Override
	public void destroy() {
		System.out.println("[Filter] destroy()");
	}
}