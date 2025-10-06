<%@ page import="java.sql.*" %>
<%@ page import="com.ecommerce.util.DBConnection" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Products</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <link rel="stylesheet" href="<%= request.getContextPath() %>/products.css?v=<%= System.currentTimeMillis() %>">
</head>
<body>
    <header>
        <h1>🛍️ Product Store</h1>
        <a href="<%= request.getContextPath() %>/cart.jsp" class="cart-button">View Cart</a>
    </header>

    <div class="product-container">
        <%
            try (Connection con = DBConnection.getConnection()) {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM products");
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String desc = rs.getString("description");
                    double price = rs.getDouble("price");
                    int stock = rs.getInt("stock");
                    String image = rs.getString("image_url");
        %>
        <div class="product-card">
            <img src="<%= image %>" alt="<%= name %>" class="product-img">
            <h3><%= name %></h3>
            <p class="desc"><%= desc %></p>
            <p class="price">₹<%= Math.round(price) %></p>
            <form action="<%= request.getContextPath() %>/CartServlet" method="post" class="cart-form">
                <input type="hidden" name="productId" value="<%= id %>">
                <input type="number" name="quantity" value="1" min="1" max="<%= stock %>" class="qty-input">
                <button type="submit" class="add-btn">Add to Cart</button>
            </form>
        </div>
        <%
                }
            } catch (Exception e) {
                out.println("<p>Error loading products: " + e.getMessage() + "</p>");
            }
        %>
    </div>
</body>
</html>