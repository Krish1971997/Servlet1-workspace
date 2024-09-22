package com.stu.todoapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stu.todoapp.dao.DatabaseOperations;
import com.stu.todoapp.dto.DataTransfer;
import com.stu.todoapp.dto.DataTranfer_login;

public class Main extends  HttpServlet  {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out=resp.getWriter();
		
		String email=req.getParameter("email");
		String password=req.getParameter("password");
		String name=req.getParameter("name");
		
		DataTranfer_login dt=new DataTranfer_login();
		dt.setEmailID(email);
		dt.setPassword(password);
		dt.setName(name);
		
		DatabaseOperations operation=new DatabaseOperations();
		
		boolean isInserted=operation.insertUser(dt);
		if (isInserted) {
			out.print("<h1>User registed successfully!</h1>");
			resp.sendRedirect("Login-form.html");
//			String login_form="<h1>Login form</h1>\r\n"
//					+ "	<hr>\r\n"
//					+ "	<form action=\"login\">\r\n"
//					+ "		<label>Enter email ID</label> <br> \r\n"
//					+ "		<input type=\"text\" name=\"emailid\">\r\n"
//					+ "		<br><br>\r\n"
//					+ "		<label>Enter the password</label><br>\r\n"
//					+ "		<input  type=\"password\" name=\"password\">\r\n"
//					+ "		<br><br>\r\n"
//					+ "		<input type=\"submit\" value=\"Login\">\r\n"
//					+ "	</form>\r\n"
//					+ "	\r\n"
//					+ "	<h4><a href=\"\">Forgot password</a></h4>";
//			out.print(login_form);
		}else
			out.print("<h1>User registration failed!</h1>");
		
	}
	
	
	
	
	
	
}
