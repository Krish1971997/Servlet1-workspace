package com.stu.studentapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stu.studentapp.dao.StudentDatabaseOperations;
import com.stu.studentapp.dto.Student;

public class RegisterStudent extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html");
		PrintWriter out=resp.getWriter();
		
		StudentDatabaseOperations operation =new StudentDatabaseOperations();
//		int id=Integer.parseInt(req.getParameter("studentID"));
		String name=req.getParameter("name");
		double marks=Double.parseDouble(req.getParameter("marks"));
		String emailid=req.getParameter("emailid");
		String password=req.getParameter("password");
		
		Student s=new Student();
//		s.setId(id);
		s.setName(name);
		s.setMarks(marks);
		s.setEmailId(emailid);
		s.setPassword(password);
		
		boolean isInserted=operation.insertStudent(s);
		if(isInserted) {
			RequestDispatcher dispatch=req.getRequestDispatcher("Login-form.html");
			dispatch.include(req, resp);
			
			out.print("<h1>Registered successfully...<br><br>\n"
					+ "Your student registration ID is "+s.getRow_number()+"..</h1>");
		}else {
			RequestDispatcher dispatch=req.getRequestDispatcher("Register_Student.html");
			dispatch.include(req, resp);
			out.print("<h1>Not Registered...</h1>");
		}
	}
}
