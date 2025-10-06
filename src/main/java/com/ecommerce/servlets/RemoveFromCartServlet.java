package com.ecommerce.servlets;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class RemoveFromCartServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
            if (cart != null) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                cart.remove(productId);
                session.setAttribute("cart", cart);
            }
        }
        response.sendRedirect("cart.jsp");
    }
}
