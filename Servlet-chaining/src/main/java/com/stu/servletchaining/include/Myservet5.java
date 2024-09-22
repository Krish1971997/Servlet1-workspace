package com.stu.servletchaining.include;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/s5")
public class Myservet5  extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html");
		PrintWriter out=resp.getWriter();
		
		out.print("<h1>This is the response from "+this.getClass().getSimpleName()+"</h1>'");
		
		RequestDispatcher dispatch1=req.getRequestDispatcher("/s4");
		dispatch1.include(req, resp);
		
	}
}
