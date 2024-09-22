package com.stu.servletchaining.forward;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/s4")
public class MyServlet4 extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html");
		PrintWriter out =resp.getWriter();
		
		out.print("<h1>This is response from "+this.getClass().getSimpleName()+"</h1>");
		
		/*
		 * RequestDispatcher requestDispacher=req.getRequestDispatcher("/s4");
		 * requestDispacher.forward(req, resp);
		 */
		
	}
}
