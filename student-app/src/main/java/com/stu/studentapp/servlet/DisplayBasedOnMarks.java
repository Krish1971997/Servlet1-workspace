package com.stu.studentapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.stu.studentapp.dao.StudentDatabaseOperations;
import com.stu.studentapp.dto.Student;

public class DisplayBasedOnMarks  extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out=resp.getWriter();
		
		HttpSession session=req.getSession(false);
		if(session!=null){
			StudentDatabaseOperations operation=new StudentDatabaseOperations();
			
			int low_mark=Integer.parseInt(req.getParameter("low_mark"));
			int high_mark=Integer.parseInt(req.getParameter("high_mark"));
			
			ArrayList<Student> displayStudentMarks=operation.getDisplayBasedonMarks(low_mark, high_mark);
			RequestDispatcher dispatch=req.getRequestDispatcher("Login-Menu.html");
			dispatch.include(req, resp);
			
			String table="<table border='1' style='width:100%'>"
					+ "<tr>"
					+ "<th style='width:10%'>ID</th>"
					+ "<th style='width:30%'>Name</>"
					+ "<th style='width:10%'>Marks</>"
					+ "<th style='width:35%'>Email</>"
					+ "<th style='width:35%'>Date</>"
//					+ "<th colspan='2' style='width:35%'>Action</>"
					+ "</tr>" 
					+ "</table>";
			out.print(table);
			if(displayStudentMarks.isEmpty()) {
				out.print("No students found between "+low_mark+" and "+high_mark+" range");
			}
			else {
			for (Student s1 : displayStudentMarks) {
				String table_detail="<table border='1' style='width:100%'>"
						+ "<tr>"
						+ "<th style='width:10%'>"+s1.getId()+"</th>"
						+ "<th style='width:30%'>"+s1.getName()+"</>"
						+ "<th style='width:10%'>"+s1.getMarks()+"</>"
						+ "<th style='width:35%'>"+s1.getEmailId()+"</>"
						+ "<th style='width:35%'>"+s1.getDate()+"</>"
						/*
						 * + "<th><a href='Update-Student.html'>Edit</a></th>" +
						 * "<th><a href='Delete-student.html'>Delete</a></th>"
						 */
						+ "</tr>" 
						+ "</table>";
				out.print(table_detail);
			}
			}
		}
		else {
			req.getRequestDispatcher("Login-form.html").forward(req, resp);
		}
		
	}
}
