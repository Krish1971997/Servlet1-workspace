package com.stu.todoapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stu.todoapp.dao.DatabaseOperations;
import com.stu.todoapp.dto.DataTransfer;

public class LogInUser extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out=resp.getWriter();
		
		String email=req.getParameter("emailid");
		String password=req.getParameter("password");
		
		DatabaseOperations operation=new DatabaseOperations();
		boolean isloginCorrect =operation.loginUser(email,password);
		
		if(isloginCorrect) {
			out.print("Login successful");	
			resp.sendRedirect("ViewServlet");
		}
		else
			out.print("Login unsuccessful");
		
	}
}
