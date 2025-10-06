package com.ecommerce.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import com.ecommerce.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CheckoutServlet extends HttpServlet{

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("cart") == null) {
            response.sendRedirect("cart.jsp");
            return;
        }

        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        String userEmail = (String) session.getAttribute("userEmail"); // store this during login
        if (userEmail == null) userEmail = "guest@example.com"; // fallback

        double grandTotal = 0;

        try (Connection con = DBConnection.getConnection()) {
            // Calculate total
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                int productId = entry.getKey();
                int qty = entry.getValue();

                PreparedStatement ps = con.prepareStatement("SELECT price FROM products WHERE id=?");
                ps.setInt(1, productId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    grandTotal += rs.getDouble("price") * qty;
                }
            }

            // Insert order
            PreparedStatement psOrder = con.prepareStatement(
                "INSERT INTO orders (user_email, total) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            psOrder.setString(1, userEmail);
            psOrder.setDouble(2, grandTotal);
            psOrder.executeUpdate();

            // Get order ID
            ResultSet rsOrder = psOrder.getGeneratedKeys();
            int orderId = 0;
            if (rsOrder.next()) {
                orderId = rsOrder.getInt(1);
            }

            // Insert order items
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                int productId = entry.getKey();
                int qty = entry.getValue();

                PreparedStatement psProd = con.prepareStatement("SELECT price FROM products WHERE id=?");
                psProd.setInt(1, productId);
                ResultSet rsProd = psProd.executeQuery();
                double price = 0;
                if (rsProd.next()) {
                    price = rsProd.getDouble("price");
                }

                PreparedStatement psItem = con.prepareStatement(
                    "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)");
                psItem.setInt(1, orderId);
                psItem.setInt(2, productId);
                psItem.setInt(3, qty);
                psItem.setDouble(4, price);
                psItem.executeUpdate();
            }

            // Clear cart
            session.removeAttribute("cart");

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h2>✅ Order placed successfully!</h2>");
            out.println("<p>Your Order ID: " + orderId + "</p>");
            out.println("<a href='products.jsp'>Continue Shopping</a>");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("❌ Checkout failed: " + e.getMessage());
        }
    }
}
