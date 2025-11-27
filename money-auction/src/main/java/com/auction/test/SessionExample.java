package com.auction.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionExample extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Session object பெறுவது
        HttpSession session = request.getSession();

        // First time access-னா ஒரு attribute set பண்ணுறோம்
        Integer count = (Integer) session.getAttribute("visitCount");
        if (count == null) {
            count = 1; // first visit
        } else {
            count = count + 1; // next visits
        }

        // Session-ல் value store பண்ணுறோம்
        session.setAttribute("visitCount", count);

        out.println("<h2>Session Example</h2>");
        out.println("<p>Number of visits: " + count + "</p>");
        out.println("<p>Session ID: " + session.getId() + "</p>");
    }
}
