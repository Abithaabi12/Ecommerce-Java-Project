package com.ecommerce.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CartServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

    public CartServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

        if (cart == null) {
            cart = new HashMap<>();
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            if (quantity > 0) {
                // Add or update product in cart
                cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
            } else {
                // If quantity is 0 or less, remove the product from cart
                cart.remove(productId);
            }

            session.setAttribute("cart", cart);
            // Redirect to cart page
            response.sendRedirect("cart.jsp");

        } catch (NumberFormatException e) {
            // If invalid input, just go back to products page
            response.sendRedirect("product.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect GET requests to the cart page
        response.sendRedirect("cart.jsp");
    }
}


