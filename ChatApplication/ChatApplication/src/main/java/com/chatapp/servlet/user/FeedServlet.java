package com.chatapp.servlet.user;

import com.chatapp.dao.*;
import com.chatapp.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/feed")
public class FeedServlet extends HttpServlet {

    private final PostDAO    postDAO    = new PostDAO();
    private final CommentDAO commentDAO = new CommentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

//    	HttpSession session = req.getSession(false);
//    	User user = (User) session.getAttribute("loggedUser");
//    	System.out.println("User - "+user);
        User user    = (User) req.getSession().getAttribute("loggedUser");
        boolean isAdmin = user.isAdmin();

        List<Post> posts = postDAO.getWallPosts(user.getId(), isAdmin);
        // Attach comments to each post
        for (Post p : posts) {
            p.setComments(commentDAO.getComments(p.getId(), isAdmin));
        }

        req.setAttribute("posts", posts);
        req.getRequestDispatcher("/jsp/user/feed.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user    = (User) req.getSession().getAttribute("loggedUser");
        String content = req.getParameter("content");

        if (content != null && !content.isBlank()) {
            postDAO.createPost(user.getId(), null, content.trim());
        }
        resp.sendRedirect(req.getContextPath() + "/feed");
    }
}
