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

public class DeleteStudent extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html");
		PrintWriter out=resp.getWriter();
		
		HttpSession session=req.getSession(false);
		StudentDatabaseOperations operation=new StudentDatabaseOperations();
		int id=Integer.parseInt(req.getParameter("id"));
		Student deleteStudent=operation.searchStudent(id);
		
		if(deleteStudent == null)
			out.print("Data not deleted because ID not found "+id);
		else {
			boolean isDeleted=operation.deleteStudent(id);
			if(isDeleted) {
				RequestDispatcher dispatch=req.getRequestDispatcher("Login-Menu.html");
				dispatch.include(req, resp);
				
				out.print("<h1><br><br><br>Data is deleted...</h1>");
		}
			else {
				RequestDispatcher dispatch=req.getRequestDispatcher("Login-Menu.html");
				dispatch.include(req, resp);
				
				out.print("<h2><br><br>Data is not deleted...<h2>");
		}}
	
	}
}
