package com.stu.querystringdemo;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ArithmaticOperation extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html");
		PrintWriter out=resp.getWriter();
		
		String number1=req.getParameter("num1");
		String number2=req.getParameter("num2");
		
		int n1=Integer.parseInt(number1);
		int n2=Integer.parseInt(number2);
		
		int add=n1+n2;
		int sub=n1-n2;
		int mul=n1*n2;
		int div=n1/n2;
		int mod=n1%n2;
		
		out.print("Addition "+n1+" and "+n2+" = "+add+"<br>");
		out.print("Subtraction "+n1+" and "+n2+" = "+sub+"<br>");
		out.print("Multiplication "+n1+" and "+n2+" = "+mul+"<br>");
		out.print("Division "+n1+" and "+n2+" = "+div+"<br>");
		out.print("Modulo "+n1+" and "+n2+" = "+mod+"<br>");
	}
}
