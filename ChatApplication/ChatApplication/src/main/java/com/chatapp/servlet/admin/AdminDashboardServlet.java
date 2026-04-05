package com.chatapp.servlet.admin;

import com.chatapp.dao.*;
import com.chatapp.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final UserDAO  userDAO  = new UserDAO();
    private final PostDAO  postDAO  = new PostDAO();
    private final GroupDAO groupDAO = new GroupDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	
    	//userDAO.insertBulkData("admin");
//    	userDAO.generatePassword("admin");
        List<User>  users  = userDAO.findAllUsers();
        List<Post>  posts  = postDAO.getWallPosts(0, true);
        List<Group> groups = groupDAO.getAllGroups();

        req.setAttribute("users",  users);
        req.setAttribute("posts",  posts);
        req.setAttribute("groups", groups);
        req.getRequestDispatcher("/jsp/admin/dashboard.jsp").forward(req, resp);
    }
}
