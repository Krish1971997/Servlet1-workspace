package com.stu.studentapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.stu.studentapp.dao.StudentDatabaseOperations;
import com.stu.studentapp.dto.Student;

public class LoginServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out=resp.getWriter();
		
		String email=req.getParameter("email");
		String password=req.getParameter("password");
		StudentDatabaseOperations operation =new StudentDatabaseOperations();
		Student isValidate=operation.loginValidate(email, password);
		if(isValidate==null) {
			String url="Login-form.html";
			RequestDispatcher dispatch=req.getRequestDispatcher("");
			dispatch.include(req, resp);
			out.print("<br><br><h1><br><br>Invalid credentials. Please check Email/Password!!! <br></h1>");
			
		}
		else
		{
			out.print("<br><h1>Welcome " + isValidate.getName() + "!!!</h1>");
			HttpSession session=req.getSession(true);
			String url="Login-Menu.html";
			RequestDispatcher dispatch=req.getRequestDispatcher(url);
			dispatch.include(req, resp);
			/*
			 * out.print("<hr>\r\n" + "<br>\r\n" + "<ul>\r\n" +
			 * "<li><a  href=\"DisplayAlldetails\">Display all details</a><br><br></li>\r\n"
			 * + "<li><a  href=\"search-student\">Search by ID</a><br><br></li>\r\n" +
			 * "<li><a  href=\"Search-ID.html\">Update student</a><br><br></li>\r\n" +
			 * "<li><a  href=\"Delete-student.html\">Delete student</a><br><br></li>\r\n" +
			 * "<li><a  href=\"\">Display based on marks</a><br><br></li>\r\n" +
			 * "<li><a  href=\"\">Logout</a><br><br></li>\r\n" + "</ul>");
			 */
		}
		
	}
}
