package com.stu.studentapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stu.studentapp.dao.StudentDatabaseOperations;

public class ForgotPassword extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StudentDatabaseOperations operation =new StudentDatabaseOperations();
		resp.setContentType("text/html");
		PrintWriter out=resp.getWriter();
		
		String email=req.getParameter("email");
		String password1=req.getParameter("password1");
		String password2=req.getParameter("password2");
		
		while(!password1.equals(password2)) {
			out.print("<h1>new password and retype password is not matching</h1>");
			resp.sendRedirect("Forgot-password.html");
		}
		
		boolean isupdatd =operation.updatePassword(password1,email);
		if(isupdatd) {
			RequestDispatcher dispatch=req.getRequestDispatcher("Login-Menu.html");
		dispatch.include(req, resp);
			out.print("<h1>Password is updated</h1>"); 
		}
		else {
		
			RequestDispatcher dispatch=req.getRequestDispatcher("Login-Menu.html");
			dispatch.include(req, resp);
			out.print("<h1>Some thing went wrong while updating the password</h1>");
		}
	}

}
