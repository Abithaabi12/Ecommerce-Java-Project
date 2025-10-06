<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.ecommerce.util.DBConnection" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>

    <title>Your Cart</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/cart.css?v=<%= System.currentTimeMillis() %>">
</head>
<body>
    <header>
        <h1>🛒 Your Shopping Cart</h1>
       <a href="<%= request.getContextPath() %>/products.jsp" class="back-button">← Continue Shopping</a>
    </header>

    <div class="cart-container">
        <%
            HttpSession session2 = request.getSession(false);
            Map<Integer, Integer> cart = null;
            if (session2 != null) {
                cart = (Map<Integer, Integer>) session2.getAttribute("cart");
            }

            double grandTotal = 0;

            if (cart != null && !cart.isEmpty()) {
        %>
        <table class="cart-table">
            <thead>
                <tr>
                    <th>Product</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Total</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
            <%
                try (Connection con = DBConnection.getConnection()) {
                    for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                        int productId = entry.getKey();
                        int qty = entry.getValue();

                        PreparedStatement ps = con.prepareStatement("SELECT * FROM products WHERE id=?");
                        ps.setInt(1, productId);
                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                            String name = rs.getString("name");
                            double price = rs.getDouble("price");
                            double total = price * qty;
                            grandTotal += total;
            %>
                <tr>
                    <td><%= name %></td>
                    <td><%= qty %></td>
                    <td>₹<%= Math.round(price) %></td>
                    <td>₹<%= Math.round(total) %></td>
                    <td>
                        <form action="RemoveFromCartServlet" method="post">
                            <input type="hidden" name="productId" value="<%= productId %>">
                            <button type="submit" class="remove-btn">Remove</button>
                        </form>
                    </td>
                </tr>
            <%
                        }
                    }
                } catch (Exception e) {
                    out.println("<tr><td colspan='5'>Error: " + e.getMessage() + "</td></tr>");
                }
            %>
            </tbody>
        </table>

        <div class="total-section">
            <h2>Grand Total: ₹<%= Math.round(grandTotal) %></h2>
            <button class="checkout-btn">Proceed to Checkout</button>
        </div>

        <%
            } else {
        %>
        <div class="empty-cart">
            <p>Your cart is empty 🛍️</p>
       <a href="<%= request.getContextPath() %>/products.jsp" class="back-button">← Continue Shopping</a>
       
        </div>
        <%
            }
        %>
    </div>
</body>
</html>