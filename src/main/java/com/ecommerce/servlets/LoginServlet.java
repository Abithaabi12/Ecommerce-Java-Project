package com.ecommerce.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ecommerce.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // ✅ Login success
                int userId = rs.getInt("id");

                HttpSession session = request.getSession();
                session.setAttribute("userId", userId);
                session.setAttribute("userEmail", email);

                response.sendRedirect("products.jsp");
            } else {
                // ❌ Login failed
                response.sendRedirect("login.html?error=1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("login.html?error=2");
        }
    }
}
