package com.application;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
	    urlPatterns = "/add",
	    initParams = {
	        @WebInitParam(name = "name", value = "Kumar")
	    }
	)
public class AddServlet extends HttpServlet{
	
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		PrintWriter out=res.getWriter();
		int i=Integer.parseInt(req.getParameter("num1"));
		int j=Integer.parseInt(req.getParameter("num2"));
		
		int k=i+j;
		
//		HttpSession session=req.getSession();
//		session.setAttribute("k",k);
		Cookie coookie=new Cookie("k",k+"");
		res.addCookie(coookie);
		
		
//		ServletContext ctx=getServletContext();
//		String name=ctx.getInitParameter("name");
//		
//		out.println(" Name " + name);
//		
		ServletConfig config=getServletConfig();
		String str=config.getInitParameter("name");
		
		out.println(" Name " + str);
		//out.println(" First Session ID: " + session.getId());

		//res.sendRedirect("sq?k="+k);
		//res.sendRedirect("sq");
		
		//out.println("Check Session ID: " + session.getId());
		
		//k=k*k;
//		out.println("Sum is :"+k);
//		req.setAttribute("k",k);
//		
//		RequestDispatcher rd=req.getRequestDispatcher("sq");
//		rd.forward(req, res);
	}
}
