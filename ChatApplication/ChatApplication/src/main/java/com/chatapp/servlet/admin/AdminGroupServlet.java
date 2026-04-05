package com.chatapp.servlet.admin;

import com.chatapp.dao.*;
import com.chatapp.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Admin group management.
 * GET  /admin/groups           → list all groups
 * GET  /admin/groups?id=N      → group detail with messages and posts
 * POST /admin/groups?action=create
 * POST /admin/groups?action=hideMsg&msgId=N
 * POST /admin/groups?action=unhideMsg&msgId=N
 * POST /admin/groups?action=addMember&groupId=N&userId=M
 * POST /admin/groups?action=removeMember&groupId=N&userId=M
 * POST /admin/groups?action=post&groupId=N  → admin post in group
 */
@WebServlet("/admin/groups")
public class AdminGroupServlet extends HttpServlet {

	private final GroupDAO groupDAO = new GroupDAO();
	private final PostDAO postDAO = new PostDAO();
	private final CommentDAO commentDAO = new CommentDAO();
	private final UserDAO userDAO = new UserDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String idStr = req.getParameter("id");
		if (idStr != null) {
			int groupId = Integer.parseInt(idStr);
			Group group = groupDAO.findById(groupId);
			List<GroupMessage> messages = groupDAO.getMessages(groupId, true);
			List<Post> posts = postDAO.getGroupPosts(groupId, 0, true);
			for (Post p : posts)
				p.setComments(commentDAO.getComments(p.getId(), true));
			List<User> allUsers = userDAO.findAllUsers();
			List<Integer> members = groupDAO.getMemberIds(groupId);

			req.setAttribute("group", group);
			req.setAttribute("messages", messages);
			req.setAttribute("posts", posts);
			req.setAttribute("allUsers", allUsers);
			req.setAttribute("members", members);
			req.getRequestDispatcher("/jsp/admin/group.jsp").forward(req, resp);
		} else {
			req.setAttribute("groups", groupDAO.getAllGroups());
			req.getRequestDispatcher("/jsp/admin/groups.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		User admin = (User) req.getSession().getAttribute("loggedUser");
		String action = req.getParameter("action");
		String back = req.getHeader("Referer");
		if (back == null)
			back = req.getContextPath() + "/admin/groups";

		switch (action == null ? "" : action) {

		case "create": {
			String name = req.getParameter("name");
			String desc = req.getParameter("description");
			if (name != null && !name.isBlank()) {
				groupDAO.createGroup(name.trim(), desc, admin.getId());
			}
			break;
		}
		case "hideMsg":
			groupDAO.hideMessage(intParam(req, "msgId"), true);
			break;
		case "unhideMsg":
			groupDAO.hideMessage(intParam(req, "msgId"), false);
			break;
		case "addMember":
			groupDAO.addMember(intParam(req, "groupId"), intParam(req, "userId"));
			break;
		case "removeMember":
			groupDAO.removeMember(intParam(req, "groupId"), intParam(req, "userId"));
			break;
		case "post": {
			String content = req.getParameter("content");
			int groupId = intParam(req, "groupId");
			// Share with specific users?
			String userIdsRaw = req.getParameter("shareWith");
			int postId = postDAO.createPost(admin.getId(), groupId, content.trim());
			if (postId > 0 && userIdsRaw != null && !userIdsRaw.isBlank()) {
				java.util.List<Integer> ids = new java.util.ArrayList<>();
				for (String s : userIdsRaw.split(",")) {
					try {
						ids.add(Integer.parseInt(s.trim()));
					} catch (NumberFormatException ignored) {
					}
				}
				if (!ids.isEmpty())
					postDAO.sharePost(postId, ids);
			}
			break;
		}
		}
		resp.sendRedirect(back);
	}

	private int intParam(HttpServletRequest req, String name) {
		String v = req.getParameter(name);
		return v != null ? Integer.parseInt(v) : -1;
	}
}
