package com.auction.servlet;

import com.auction.model.User;
import com.auction.util.DatabaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

//@WebServlet("/jsp/user/*")
public class UserServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if ("/viewUsers".equals(path)) {
            try (Connection conn = DatabaseUtil.getConnection()) {
                String sql = "SELECT * FROM users WHERE role = 'USER'";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                List<User> users = new ArrayList<>();
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    users.add(user);
                }

                request.setAttribute("users", users);
                request.getRequestDispatcher("/jsp/view_users.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Error fetching users");
                request.getRequestDispatcher("/jsp/view_users.jsp").forward(request, response);
            }
        }
    }
}
