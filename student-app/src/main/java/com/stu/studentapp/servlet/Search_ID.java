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

public class Search_ID extends HttpServlet {

	public static int sid=0;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html");
		PrintWriter out=resp.getWriter();
		HttpSession session=req.getSession(false);
		
		StudentDatabaseOperations operation=new StudentDatabaseOperations();
		
		int id=Integer.parseInt(req.getParameter("id"));
		sid=id;
		Student search=operation.searchStudent(id);
		if(search==null || search.getId()==0 ||search.getName()==null) {
			out.print("Data cannot be be updated  bacause data not found for id "+id);
			RequestDispatcher dispatch=req.getRequestDispatcher("Login-Menu.html");
			dispatch.include(req, resp);
			}
		else  {
			RequestDispatcher dispatch=req.getRequestDispatcher("Update-Student.html");
			dispatch.forward(req, resp);
		}
		
	}
}
