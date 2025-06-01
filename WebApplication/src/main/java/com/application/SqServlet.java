package com.application;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/sq")
public class SqServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		//int k=Integer.parseInt(req.getParameter("k"));
		HttpSession session=req.getSession();
		int k=1;
		
		Cookie cookies[]=req.getCookies();
		for(Cookie c:cookies) {
			if("k".equals(c.getName())) {
				k=Integer.parseInt(c.getValue());
			}
		}
		
//		if(session!=null) {
//			k=(Integer)session.getAttribute("k");
//		}
		PrintWriter out=res.getWriter();
		
		//int k=(int) req.getAttribute("k");
		
		out.println("Result : "+k*k);
		out.println("Session ID: " + session.getId());
		out.println("Is new: " + session.isNew());

		
	}

}
