<%@ page import="java.sql.*" %>
<%@ page import="com.ecommerce.util.DBConnection" %>
<html>
<body>
<h2>Test Products</h2>
<%
try (Connection con = DBConnection.getConnection()) {
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM products");
    while (rs.next()) {
        out.println(rs.getString("name") + " - ₹" + rs.getDouble("price") + "<br>");
    }
} catch(Exception e) {
    out.println("Error: " + e.getMessage());
}
%>
</body>
</html>