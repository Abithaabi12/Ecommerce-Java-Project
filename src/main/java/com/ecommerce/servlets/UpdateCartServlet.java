package com.ecommerce.servlets;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UpdateCartServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

        if (cart != null) {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            if (quantity > 0) {
                cart.put(productId, quantity);
            } else {
                cart.remove(productId);
            }

            session.setAttribute("cart", cart);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET not supported. Use POST.");
    }
}