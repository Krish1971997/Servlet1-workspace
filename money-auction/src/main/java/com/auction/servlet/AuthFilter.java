package com.auction.servlet;

import com.auction.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/jsp/admin/*","/jsp/user/*","/jsp/draw/*","/jsp/admin_dashboard.jsp","/jsp/add_user.jsp"})
public class AuthFilter implements Filter {
    private static final String LOGIN_PAGE = "/jsp/login.jsp";
    private static final String USER_DASHBOARD = "/jsp/user_dashboard.jsp";
    private static final String ADMIN_PREFIX = "/jsp/admin";
    private static final String USER_PREFIX = "/jsp/user";
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String path = httpRequest.getRequestURI();
        System.out.println("requestURI : "+path);
        String contextPath = httpRequest.getContextPath();
        System.out.println("Context : "+contextPath);
        String pathinfo = httpRequest.getPathInfo();
        System.out.println("pathinfo : "+pathinfo);

        if (session == null || session.getAttribute("user") == null) {
            httpResponse.sendRedirect(contextPath+LOGIN_PAGE);
            return;
        }
        
        if(session!=null) {
        	httpResponse.setContentType("text/html");
        	httpResponse.setHeader("Cache-Control","no-store,no-cache,must-revalidate");
        	httpResponse.setHeader("Pragma", "no-cache");
        	httpResponse.setDateHeader("Expires", 0);
        }
        
        User user = (User) session.getAttribute("user");

        if (path.startsWith("/admin") && !"ADMIN".equals(user.getRole())) {
            httpResponse.sendRedirect(contextPath+USER_DASHBOARD);
            return;
        }

        chain.doFilter(request, response);
    }
}
