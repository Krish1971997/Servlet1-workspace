<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Setup Auction</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="container">
    <h2>Setup New Auction</h2>
    <% if (request.getAttribute("message") != null) { %>
        <div class="alert alert-success"><%= request.getAttribute("message") %></div>
    <% } %>
    <form action="/admin/setupAuction" method="post">
        <div class="form-group">
            <label>Total Months:</label>
            <input type="number" name="totalMonths" min="1" required>
        </div>
        <button type="submit">Start Auction</button>
    </form>
</div>
</body>
</html>
