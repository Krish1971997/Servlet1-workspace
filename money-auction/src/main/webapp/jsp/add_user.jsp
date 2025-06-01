<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Add User</title>
    <link rel="stylesheet" href="/css/style.css">
    <script src="/js/script.js"></script>
</head>
<body>
<div class="container">
    <h2>Add User</h2>
    <% if (request.getAttribute("message") != null) { %>
        <div class="alert alert-success"><%= request.getAttribute("message") %></div>
    <% } %>
    <form action="admin/addUser" method="post" onsubmit="return validateForm()">
        <div class="form-group">
            <label>Name:</label>
            <input type="text" name="name" required>
        </div>
        <div class="form-group">
            <label>Email:</label>
            <input type="email" name="email" required>
        </div>
        <div class="form-group">
            <label>Password:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <div class="form-group">
            <label>Confirm Password:</label>
            <input type="password" id="confirmPassword" required>
        </div>
        <div class="form-group">
            <label>Role:</label>
            <select name="role">
                <option value="USER">User</option>
                <option value="ADMIN">Admin</option>
            </select>
        </div>
        <button type="submit">Add User</button>
    </form>
</div>
</body>
</html>
