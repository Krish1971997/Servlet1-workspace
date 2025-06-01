<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>View Users</title>
    <link rel="stylesheet" href="/css/style.css">
    <script src="/js/script.js"></script>
</head>
<body>
<div class="container">
    <h2>Users List</h2>
    <table>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="user" items="${users}">
            <tr>
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td>${user.role}</td>
                <td>
                    <c:if test="${sessionScope.user.role == 'ADMIN'}">
                        <button onclick="confirmDelete(${user.id})">Delete</button>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
