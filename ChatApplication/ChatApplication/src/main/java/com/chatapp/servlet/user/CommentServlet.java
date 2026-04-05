package com.chatapp.servlet.user;

import com.chatapp.dao.CommentDAO;
import com.chatapp.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {

	private final CommentDAO commentDAO = new CommentDAO();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		User user = (User) req.getSession().getAttribute("loggedUser");
		String postIdStr = req.getParameter("postId");
		String content = req.getParameter("content");
		String redirect = req.getParameter("redirect");

		if (postIdStr != null && content != null && !content.isBlank()) {
			int postId = Integer.parseInt(postIdStr);
			commentDAO.addComment(postId, user.getId(), content.trim());
		}

		// Go back to wherever the user came from (feed, group page, etc.)
		String back = (redirect != null && !redirect.isBlank()) ? redirect : req.getContextPath() + "/feed";
		resp.sendRedirect(back);
	}
}
