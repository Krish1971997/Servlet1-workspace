package com.auction.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auction.model.DrawResult;
import com.auction.model.User;
import com.auction.util.DatabaseUtil;
import com.auction.util.EmailUtil;

@WebServlet("/jsp/draw/*")
public class DrawServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if ("/results".equals(path)) {
            try (Connection conn = DatabaseUtil.getConnection()) {
                String sql = "SELECT dr.*, u.name FROM draw_results dr JOIN users u ON dr.user_id = u.id";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                List<DrawResult> results = new ArrayList<>();
                while (rs.next()) {
                    DrawResult result = new DrawResult();
                    result.setMonth(rs.getInt("month"));
                    result.setUserName(rs.getString("name"));
                    result.setAmount(rs.getDouble("amount"));
                    result.setDrawDate(rs.getTimestamp("draw_date"));
                    results.add(result);
                }

                request.setAttribute("results", results);
                request.getRequestDispatcher("/jsp/draw_result.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Error fetching draw results");
                request.getRequestDispatcher("/jsp/draw_result.jsp").forward(request, response);
            }
        } else {
            try (Connection conn = DatabaseUtil.getConnection()) {
                // Get current auction
                String auctionSql = "SELECT * FROM auctions ORDER BY id DESC LIMIT 1";
                PreparedStatement auctionStmt = conn.prepareStatement(auctionSql);
                ResultSet auctionRs = auctionStmt.executeQuery();

                if (!auctionRs.next()) {
                    request.setAttribute("error", "No active auction");
                    request.getRequestDispatcher("/jsp/admin_dashboard.jsp").forward(request, response);
                    return;
                }

                int auctionId = auctionRs.getInt("id");
                int currentMonth = auctionRs.getInt("current_month");
                int totalMonths = auctionRs.getInt("total_months");

                if (currentMonth > totalMonths) {
                    request.setAttribute("error", "Auction completed. Please start a new auction.");
                    request.getRequestDispatcher("/jsp/admin_dashboard.jsp").forward(request, response);
                    return;
                }

                // Get eligible users (not yet drawn)
                String userSql = "SELECT u.* FROM users u WHERE u.role = 'USER' AND u.id NOT IN (SELECT user_id FROM draw_results WHERE auction_id = ?)";
                PreparedStatement userStmt = conn.prepareStatement(userSql);
                userStmt.setInt(1, auctionId);
                ResultSet userRs = userStmt.executeQuery();

                List<User> eligibleUsers = new ArrayList<>();
                while (userRs.next()) {
                    User user = new User();
                    user.setId(userRs.getInt("id"));
                    user.setName(userRs.getString("name"));
                    user.setEmail(userRs.getString("email"));
                    eligibleUsers.add(user);
                }

                if (eligibleUsers.isEmpty()) {
                    request.setAttribute("error", "No eligible users for draw");
                    request.getRequestDispatcher("/jsp/admin_dashboard.jsp").forward(request, response);
                    return;
                }

                // Random draw
                Random random = new Random();
                User winner = eligibleUsers.get(random.nextInt(eligibleUsers.size()));
                double amount = 1000.00; // Example amount

                // Save draw result
                String drawSql = "INSERT INTO draw_results (auction_id, user_id, month, amount) VALUES (?, ?, ?, ?)";
                PreparedStatement drawStmt = conn.prepareStatement(drawSql);
                drawStmt.setInt(1, auctionId);
                drawStmt.setInt(2, winner.getId());
                drawStmt.setInt(3, currentMonth);
                drawStmt.setDouble(4, amount);
                drawStmt.executeUpdate();

                // Update auction current month
                String updateAuctionSql = "UPDATE auctions SET current_month = current_month + 1 WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateAuctionSql);
                updateStmt.setInt(1, auctionId);
                updateStmt.executeUpdate();

                // Send email notification
                String subject = "Money Auction Draw Result";
                String body = "Congratulations " + winner.getName() + "! You have won the auction for month " + currentMonth + " with amount $" + amount;
                EmailUtil.sendEmail(winner.getEmail(), subject, body);

                request.setAttribute("message", "Draw completed. Winner: " + winner.getName());
                request.getRequestDispatcher("/jsp/admin_dashboard.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Error performing draw");
                request.getRequestDispatcher("/jsp/admin_dashboard.jsp").forward(request, response);
            }
        }
    }
}
