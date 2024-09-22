package com.stu.querystringdemo;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DisplayFullNameServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");

		PrintWriter out = resp.getWriter();
		out.print("<h1>This is the response of " + this.getClass().getSimpleName() + "</h1>");
		String fm=req.getParameter("fname");
		out.print("<h2>First name is "+fm+"</h2>");
		
		String lm=req.getParameter("lname");
		out.print("<h2>Last name is "+lm+"</h2>");
		
		String fullName=fm+" "+lm;
		out.print("<h3> Full name is "+fullName+"</h3>");
		

	}

}
