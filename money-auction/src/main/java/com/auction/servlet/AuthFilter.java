package com.auction.servlet;

import com.auction.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            httpResponse.sendRedirect("/jsp/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        String path = httpRequest.getRequestURI();

        if (path.startsWith("/admin") && !"ADMIN".equals(user.getRole())) {
            httpResponse.sendRedirect("/jsp/user_dashboard.jsp");
            return;
        }

        chain.doFilter(request, response);
    }
}
