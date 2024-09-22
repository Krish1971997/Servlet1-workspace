package com.stu.studentapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.stu.studentapp.dao.StudentDatabaseOperations;
import com.stu.studentapp.dto.Student;

public class UpdateStudent  extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out=resp.getWriter();
		
		HttpSession session=req.getSession(false);
		StudentDatabaseOperations operation=new StudentDatabaseOperations();
		
		Student stu=operation.searchStudent(Search_ID.sid);
		int hashCodeBeforeChange=stu.hashCode();
		String name=req.getParameter("name");
		String email=req.getParameter("email");
		
		//double marks=0.00;
		String mark=req.getParameter("marks");
		//if(mark!=null || mark!="" ) {
		double	marks=Double.parseDouble(mark);
		stu.setMarks(marks);
// 		}
	
//		if(name!=null || name!="") 
			stu.setName(name); 
//		if(email!=null||email!="")
			stu.setEmailId(email); 
		
		int hashCodeAfterChange=stu.hashCode();
		
		if(hashCodeBeforeChange!=hashCodeAfterChange) {
			boolean isUpdated=operation.updateStudentdetails(stu);
			if (isUpdated) {
				
				RequestDispatcher dispatch=req.getRequestDispatcher("Login-Menu.html");
				dispatch.include(req, resp);
				
				out.print("<br><br><h1>Data Updated !!!</h1>");
				
				
			} else {
				RequestDispatcher dispatch=req.getRequestDispatcher("Login-Menu.html");
				dispatch.include(req, resp);
				
				out.print("<h1><br><br>Data is not updated !!!</h1>");
			}
		}
	}

	
}
