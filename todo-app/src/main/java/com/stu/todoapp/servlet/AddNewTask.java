package com.stu.todoapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stu.todoapp.dao.DatabaseOperations;
import com.stu.todoapp.dto.DataTransfer;

public class AddNewTask extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	resp.setContentType("text/html");	
	PrintWriter out =resp.getWriter();
	DatabaseOperations operation=new DatabaseOperations();
	
	//int id=Integer.parseInt(req.getParameter("id"));
	String name=req.getParameter("name");
	String task=req.getParameter("task");
	String task_desc=req.getParameter("task_desc");
//	String date=req.getParameter("date");
	DataTransfer dt=new DataTransfer();
	//dt.setID(id);
	dt.setName(name);
	dt.setTask(task);
	dt.setTask_discription(task_desc);
	
	boolean isInserted_task=operation.insertNewTask(dt);
	if (isInserted_task) {
		out.print("<h3>New task has been added..</h3><hr>");
		out.print("<h3><a href=\"Add-new-task.html\">Click to add new task</a></h3>");
		//resp.sendRedirect("Add-new-task.html");
		out.print("<h3><a href=\"ViewServlet\">Home Page</a></h3>");
	}
	else
		out.print("<h3>New task has not been added</h3>");
	
	}
}
