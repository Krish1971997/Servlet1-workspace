package com.application;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SqServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		//int k=Integer.parseInt(req.getParameter("k"));
		HttpSession session=req.getSession(false);
		int k=(Integer)session.getAttribute("k");
		PrintWriter out=res.getWriter();
		
		//int k=(int) req.getAttribute("k");
		
		out.print("Result : "+k*k);
		
	}

}
