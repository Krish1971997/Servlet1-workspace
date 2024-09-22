package com.stu.todoapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stu.todoapp.dao.DatabaseOperations;
import com.stu.todoapp.dto.DataTranfer_login;
import com.stu.todoapp.dto.DataTransfer;

public class ViewServlet  extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DatabaseOperations operation=new DatabaseOperations();
		DataTranfer_login dt=new DataTranfer_login();
		resp.setContentType("text/html");
		PrintWriter out=resp.getWriter();
		
		out.print("<h3 display: inline; ><h3><a href=\"Register-form.html\">Add new user</a></h3>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "&nbsp;&nbsp;"
				+ dt.getName()+"</h3>") ;
		
		out.print("<h3><a href=\"Add-new-task.html\">Add new task</a></h3>");
		out.print("<h1>Employees tasks</h1>");
		
		ArrayList<DataTransfer> listOfTasks=operation.listOfTasks();
		String table_header="<table border='1' style=\"width:100%\">"
				+ "<tr>"
				+ "<th>ID</th>"
				+ "<th>Name</th>"
				+ "<th>Task</th>"
				+ "<th>Task_discription</th>"
				+ "<th>date</th>"
				+ "</tr>"
				+ "</table>";
		out.print(table_header);
		for (DataTransfer dataTransfer : listOfTasks) {
			String table_details="<table border='1' style=\"width:100%\">"
					+ "<tr>"
					+ "<th>"+dataTransfer.getID()+"</th>"
					+ "<th>"+dataTransfer.getName()+"</th>"
					+ "<th>"+dataTransfer.getTask()+"</th>"
					+ "<th>"+dataTransfer.getTask_discription()+"</th>"
					+ "<th>"+dataTransfer.getDate()+"</th>"
					+ "</tr>"
					+ "</table>";
			out.print(table_details);
		}

	
	}
}
