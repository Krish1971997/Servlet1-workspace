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

public class SearchStudentServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();

		HttpSession session=req.getSession(false);
		// String id=req.getParameter("sid");
		int sid = Integer.parseInt(req.getParameter("sid"));

		StudentDatabaseOperations operation = new StudentDatabaseOperations();
		Student student = operation.searchStudent(sid);
		if (student != null) {

			String url = "Login-Menu.html";
			RequestDispatcher dispatch = req.getRequestDispatcher(url);
			dispatch.include(req, resp);

			String table = "<table border='1'><br><br>" + "<tr>" + "<th style='width:10%'>ID</th>"
					+ "<th style='width:30%'>Name</th>" + "<th style='width:10%'>Marks</th>"
					+ "<th style='width:35%'>Email</th>" + "<th style='width:35%'>Date</th>" + "</tr>"
					+ "<td style='width:10%'>" + student.getId() + "</td>" + "	<td style='width:30%'>"
					+ student.getName() + "</td>" + "<td style='width:10%'>" + student.getMarks() + "</td>"
					+ "<td style='width:35%'>" + student.getEmailId() + "</td>" + "<td style='width:35%'>"
					+ student.getDate() + "</td>" + "<tr>" + "</tr>" + "</table>";

			out.print(table);
		} else {
			String url = "Login-Menu.html";
			RequestDispatcher dispatch = req.getRequestDispatcher(url);
			dispatch.include(req, resp);
			out.print("No records found for student ID " + sid);
		}
	}

}
