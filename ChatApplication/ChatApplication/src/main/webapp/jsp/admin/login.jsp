<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Admin Login · ChatApp</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="auth-wrap">
  <div class="auth-box">
    <div class="auth-title" style="color:var(--accent2)">⚙️ Admin Login</div>
    <div class="auth-subtitle">Restricted access — authorized personnel only</div>

    <c:if test="${not empty error}">
      <div class="alert alert-error">${error}</div>
    </c:if>
    <c:if test="${not empty success}">
      <div class="alert alert-success">${success}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/admin/login">
      <div class="form-group">
        <label>Username or Email</label>
        <input type="text" name="identifier" required autofocus>
      </div>
      <div class="form-group">
        <label>Password</label>
        <input type="password" name="password" required>
      </div>
      <button type="submit" class="btn btn-primary" style="width:100%;justify-content:center;padding:.7rem">
        Admin Sign In
      </button>
    </form>

    <div class="auth-footer">
      <a href="${pageContext.request.contextPath}/admin/signup">Register as admin</a>
      &nbsp;·&nbsp;
      <a href="${pageContext.request.contextPath}/login">User login</a>
    </div>
  </div>
</div>
</body>
</html>
