package com.auction.servlet;

import com.auction.model.User;
import com.auction.util.DatabaseUtil;
import com.auction.util.PasswordUtil;

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

@WebServlet("/jsp/admin/*")
public class AdminServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if ("/addUser".equals(path)) {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String role = request.getParameter("role");

            try (Connection conn = DatabaseUtil.getConnection()) {
                String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, password);//PasswordUtil.hashPassword(password));
                stmt.setString(4, role);
                stmt.executeUpdate();

                request.setAttribute("message", "User added successfully");
                request.getRequestDispatcher("/jsp/add_user.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Error adding user");
                request.getRequestDispatcher("/jsp/add_user.jsp").forward(request, response);
            }
        } else if ("/setupAuction".equals(path)) {
            int totalMonths = Integer.parseInt(request.getParameter("totalMonths"));

            try (Connection conn = DatabaseUtil.getConnection()) {
                String sql = "INSERT INTO auctions (total_months, current_month) VALUES (?, 1)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, totalMonths);
                stmt.executeUpdate();

                request.setAttribute("message", "Auction setup successfully");
                request.getRequestDispatcher("/jsp/admin_dashboard.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Error setting up auction");
                request.getRequestDispatcher("/jsp/auction_setup.jsp").forward(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if ("/viewUsers".equals(path)) {
            try (Connection conn = DatabaseUtil.getConnection()) {
                String sql = "SELECT * FROM users";
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
        } else if ("/delete".equals(path)) {
            int userId = Integer.parseInt(request.getParameter("userId"));

            try (Connection conn = DatabaseUtil.getConnection()) {
                String sql = "DELETE FROM users WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.executeUpdate();

                response.sendRedirect("/admin/viewUsers");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Error deleting user");
                request.getRequestDispatcher("/jsp/view_users.jsp").forward(request, response);
            }
        }
    }
}
