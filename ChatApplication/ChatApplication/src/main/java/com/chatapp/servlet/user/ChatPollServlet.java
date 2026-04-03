package com.chatapp.servlet.user;

import com.chatapp.dao.GroupDAO;
import com.chatapp.model.GroupMessage;
import com.chatapp.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * AJAX polling endpoint for group chat.
 * GET /chat/poll?groupId=N&afterId=M
 * Returns JSON array of new messages.
 */
@WebServlet("/chat/poll")
public class ChatPollServlet extends HttpServlet {

    private final GroupDAO groupDAO = new GroupDAO();
    private static final Gson GSON = new GsonBuilder().create();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("MMM d, HH:mm");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("loggedUser");
        if (user == null) { resp.setStatus(401); return; }

        int groupId = Integer.parseInt(req.getParameter("groupId"));
        int afterId = Integer.parseInt(req.getParameter("afterId"));
        boolean isAdmin = user.isAdmin();

        // Must be a member (admins bypass)
        if (!isAdmin && !groupDAO.isMember(groupId, user.getId())) {
            resp.setStatus(403); return;
        }

        List<GroupMessage> msgs = groupDAO.getNewMessages(groupId, afterId, isAdmin);

        // Convert to simple map for JSON
        List<Map<String, Object>> result = msgs.stream().map(m -> {
            Map<String, Object> map = new java.util.LinkedHashMap<>();
            map.put("id",       m.getId());
            map.put("sender",   m.getSenderUsername());
            map.put("message",  m.getMessage());
            map.put("hidden",   m.isHidden());
            map.put("time",     m.getCreatedAt() != null
                                    ? m.getCreatedAt().format(FMT) : "");
            map.put("mine",     m.getSenderId() == user.getId());
            return map;
        }).toList();

        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(GSON.toJson(result));
    }
}
