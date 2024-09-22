package com.stu.studentapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class Logout extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html");
		PrintWriter out=resp.getWriter();
		
		HttpSession session=req.getSession(false);
		if(session!=null) {
			session.invalidate();
			out.print("<!DOCTYPE html>\r\n"
					+ "<html>\r\n"
					+ "<head>\r\n"
					+ "<meta charset=\"UTF-8\">\r\n"
					+ "<title>Logout</title>\r\n"
					+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"Style/home.css\">\r\n"
					+ "</head>\r\n"
					+ "<body>\r\n"
					+ "\r\n"
					+ "<h1>Logged out successfully!!!!</h1>\r\n"
					+ "\r\n"
					+ "</body>\r\n"
					+ "</html>");
			RequestDispatcher dispatch=req.getRequestDispatcher("Login-form.html");
			dispatch.include(req, resp);
		}
		else {
			
		}
	
	}
}
