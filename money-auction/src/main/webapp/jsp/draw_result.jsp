<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Draw Results</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="container">
    <h2>Draw Results</h2>
    <table>
        <tr>
            <th>Month</th>
            <th>User</th>
            <th>Amount</th>
            <th>Date</th>
        </tr>
        <c:forEach var="result" items="${results}">
            <tr>
                <td>${result.month}</td>
                <td>${result.userName}</td>
                <td>${result.amount}</td>
                <td>${result.drawDate}</td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
