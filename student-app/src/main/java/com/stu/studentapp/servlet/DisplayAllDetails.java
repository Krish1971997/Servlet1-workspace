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

public class DisplayAllDetails extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out=resp.getWriter();
		
			
		HttpSession session=req.getSession(false);
		if(session!=null) {
			req.getRequestDispatcher("Login-Menu.html").include(req, resp);
		StudentDatabaseOperations operation=new StudentDatabaseOperations();
		ArrayList<Student> allstudenets=operation.getAllStudents();
		
		
		/*
		 * out.print("<hr>\r\n" + "<br>\r\n" + "<ul>\r\n" +
		 * "<li><a  href=\"DisplayAlldetails\">Display all details</a><br><br></li>\r\n"
		 * + "<li><a  href=\"search-student\">Search by ID</a><br><br></li>\r\n" +
		 * "<li><a  href=\"\">Update student</a><br><br></li>\r\n" +
		 * "<li><a  href=\"\">Delete student</a><br><br></li>\r\n" +
		 * "<li><a  href=\"\">Display based on marks</a><br><br></li>\r\n" +
		 * "<li><a  href=\"\">Logout</a><br><br></li>\r\n" + "</ul>");
		 */
		
		String table="<table border='1' style='width:100%'>"
				+ "<tr>"
				+ "<th style='width:10%'>ID</th>"
				+ "<th style='width:25%'>Name</>"
				+ "<th style='width:10%'>Marks</>"
				+ "<th style='width:25%'>Email</>"
				+ "<th style='width:25%'>Date</>"
				+ "<th colspan='2' style='width:35%'>Action</th>"
				+ "</tr>" 
				+ "</table>";
		out.print(table);
		for (Student s1 : allstudenets) {
			String table_detail="<table border='1' style='width:100%'>"
					+ "<tr>"
					+ "<th style='width:10%'>"+s1.getId()+"</th>"
					+ "<th style='width:25%'>"+s1.getName()+"</>"
					+ "<th style='width:10%'>"+s1.getMarks()+"</>"
					+ "<th style='width:25%'>"+s1.getEmailId()+"</>"
					+ "<th style='width:25%'>"+s1.getDate()+"</>"
					+ "<th><a href='Update-Student.html' width:25% >Edit</a></th>"
					+ "<th><a href='Delete-student.html' width:25%>Delete</a></th>"
					+ "</tr>" 
					+ "</table>";
			out.print(table_detail);
		}
		}
		else
			req.getRequestDispatcher("Login-form.html").include(req, resp);
		
	}
}
