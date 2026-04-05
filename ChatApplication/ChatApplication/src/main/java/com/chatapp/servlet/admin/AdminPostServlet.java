package com.chatapp.servlet.admin;

import com.chatapp.dao.*;
import com.chatapp.model.*;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

/**
 * Admin post management actions.
 * POST /admin/posts?action=hide&postId=N
 * POST /admin/posts?action=unhide&postId=N
 * POST /admin/posts?action=share&postId=N&userIds=1,2,3
 * POST /admin/posts?action=delete&postId=N
 * POST /admin/posts?action=create   (wall or group post by admin)
 */
@WebServlet("/admin/posts")
public class AdminPostServlet extends HttpServlet {

	private final PostDAO postDAO = new PostDAO();
	private final CommentDAO commentDAO = new CommentDAO();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		User admin = (User) req.getSession().getAttribute("loggedUser");
		String action = req.getParameter("action");
		String back = req.getHeader("Referer");
		if (back == null)
			back = req.getContextPath() + "/admin/dashboard";

		switch (action == null ? "" : action) {

		case "hide":
			postDAO.setHidden(intParam(req, "postId"), true);
			break;

		case "unhide":
			postDAO.setHidden(intParam(req, "postId"), false);
			break;

		case "delete":
			postDAO.deletePost(intParam(req, "postId"));
			break;

		case "share": {
			int postId = intParam(req, "postId");
			String userIdsRaw = req.getParameter("userIds"); // e.g. "1,3,7"
			List<Integer> ids = new ArrayList<>();
			if (userIdsRaw != null && !userIdsRaw.isBlank()) {
				for (String s : userIdsRaw.split(",")) {
					try {
						ids.add(Integer.parseInt(s.trim()));
					} catch (NumberFormatException ignored) {
					}
				}
			}
			postDAO.sharePost(postId, ids);
			break;
		}

		case "create": {
			String content = req.getParameter("content");
			String gidStr = req.getParameter("groupId");
			Integer groupId = (gidStr != null && !gidStr.isBlank()) ? Integer.parseInt(gidStr) : null;
			if (content != null && !content.isBlank()) {
				postDAO.createPost(admin.getId(), groupId, content.trim());
			}
			break;
		}

		case "hideComment":
			commentDAO.setHidden(intParam(req, "commentId"), true);
			break;

		case "unhideComment":
			commentDAO.setHidden(intParam(req, "commentId"), false);
			break;

		case "deleteComment":
			commentDAO.deleteComment(intParam(req, "commentId"));
			break;
		}

		resp.sendRedirect(back);
	}

	private int intParam(HttpServletRequest req, String name) {
		String v = req.getParameter(name);
		return v != null ? Integer.parseInt(v) : -1;
	}
}
