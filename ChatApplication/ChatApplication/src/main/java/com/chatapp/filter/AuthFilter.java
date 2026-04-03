package com.chatapp.filter;

import com.chatapp.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Redirects unauthenticated requests to /login.
 * Allows /login, /signup, /css/*, /js/* through without auth.
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getServletPath();

        // Public paths
        if (path.startsWith("/css") || path.startsWith("/js")
                || path.equals("/login")  || path.equals("/signup")
                || path.equals("/admin/login") || path.equals("/admin/signup")) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (user == null) {
            // Admin path → admin login, else user login
            String redirect = path.startsWith("/admin") ? request.getContextPath() + "/admin/login"
                                                        : request.getContextPath() + "/login";
            response.sendRedirect(redirect);
            return;
        }

        // Admin path accessed by non-admin
        if (path.startsWith("/admin") && !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/feed");
            return;
        }

        chain.doFilter(req, res);
    }
}
