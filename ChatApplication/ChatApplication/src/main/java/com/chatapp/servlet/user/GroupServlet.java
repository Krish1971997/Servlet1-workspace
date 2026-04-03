package com.chatapp.servlet.user;

import com.chatapp.dao.*;
import com.chatapp.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Handles:
 *   GET  /groups          → list groups the user belongs to
 *   GET  /groups?id=N     → group chat/feed page
 *   POST /groups?action=join&groupId=N   → join a group
 *   POST /groups?action=leave&groupId=N  → leave a group
 *   POST /groups?action=chat&groupId=N   → send chat message
 *   POST /groups?action=post&groupId=N   → create post in group
 */
@WebServlet("/groups")
public class GroupServlet extends HttpServlet {

    private final GroupDAO   groupDAO   = new GroupDAO();
    private final PostDAO    postDAO    = new PostDAO();
    private final CommentDAO commentDAO = new CommentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user    = (User) req.getSession().getAttribute("loggedUser");
        boolean isAdmin = user.isAdmin();
        String idStr = req.getParameter("id");

        if (idStr != null) {
            // ── Single group page ──────────────────────────────
            int groupId = Integer.parseInt(idStr);
            Group group = groupDAO.findById(groupId);
            if (group == null) { resp.sendRedirect(req.getContextPath() + "/groups"); return; }

            // Members can see the group; admins always can
            if (!isAdmin && !groupDAO.isMember(groupId, user.getId())) {
                resp.sendRedirect(req.getContextPath() + "/groups");
                return;
            }

            List<GroupMessage> messages = groupDAO.getMessages(groupId, isAdmin);
            List<Post>         posts    = postDAO.getGroupPosts(groupId, user.getId(), isAdmin);
            for (Post p : posts) p.setComments(commentDAO.getComments(p.getId(), isAdmin));

            req.setAttribute("group",    group);
            req.setAttribute("messages", messages);
            req.setAttribute("posts",    posts);
            req.setAttribute("isMember", groupDAO.isMember(groupId, user.getId()));
            req.getRequestDispatcher("/jsp/user/group.jsp").forward(req, resp);
        } else {
            // ── All groups ─────────────────────────────────────
            List<Group> myGroups  = groupDAO.getGroupsForUser(user.getId());
            List<Group> allGroups = groupDAO.getAllGroups();
            req.setAttribute("myGroups",  myGroups);
            req.setAttribute("allGroups", allGroups);
            req.getRequestDispatcher("/jsp/user/groups.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user    = (User) req.getSession().getAttribute("loggedUser");
        String action  = req.getParameter("action");
        String gidStr  = req.getParameter("groupId");
        int groupId = (gidStr != null) ? Integer.parseInt(gidStr) : -1;

        switch (action == null ? "" : action) {
            case "join":
                groupDAO.addMember(groupId, user.getId());
                resp.sendRedirect(req.getContextPath() + "/groups?id=" + groupId);
                break;

            case "leave":
                groupDAO.removeMember(groupId, user.getId());
                resp.sendRedirect(req.getContextPath() + "/groups");
                break;

            case "chat":
                String msg = req.getParameter("message");
                if (msg != null && !msg.isBlank() && groupDAO.isMember(groupId, user.getId())) {
                    groupDAO.sendMessage(groupId, user.getId(), msg.trim());
                }
                resp.sendRedirect(req.getContextPath() + "/groups?id=" + groupId);
                break;

            case "post":
                String content = req.getParameter("content");
                if (content != null && !content.isBlank() && groupDAO.isMember(groupId, user.getId())) {
                    postDAO.createPost(user.getId(), groupId, content.trim());
                }
                resp.sendRedirect(req.getContextPath() + "/groups?id=" + groupId);
                break;

            default:
                resp.sendRedirect(req.getContextPath() + "/groups");
        }
    }
}
