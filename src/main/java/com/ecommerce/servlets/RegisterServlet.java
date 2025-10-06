package com.ecommerce.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ecommerce.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection con = DBConnection.getConnection()) {
            String query = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                // ✅ Registration success → redirect to login page with success message
                response.sendRedirect("login.html?success=1");
            } else {
                // ❌ Registration failed → back to register page
                response.sendRedirect("register.html?error=1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("register.html?error=2");
        }
    }
}