<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Sign Up · ChatApp</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="auth-wrap">
  <div class="auth-box">
    <div class="auth-title">Create account</div>
    <div class="auth-subtitle">Join ChatApp and start connecting</div>

    <c:if test="${not empty error}">
      <div class="alert alert-error">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/signup" novalidate>
      <div class="form-group">
        <label>Username</label>
        <input type="text" name="username" placeholder="e.g. john_doe" required autofocus>
        <small style="color:var(--muted);font-size:.78rem">3-50 chars, letters/digits/underscore only</small>
      </div>
      <div class="form-group">
        <label>Email address</label>
        <input type="email" name="email" placeholder="you@example.com" required>
      </div>
      <div class="form-group">
        <label>Password</label>
        <input type="password" name="password" placeholder="Min 6 characters" required>
      </div>
      <div class="form-group">
        <label>Confirm Password</label>
        <input type="password" name="confirmPassword" placeholder="Repeat password" required>
      </div>
      <button type="submit" class="btn btn-primary" style="width:100%;justify-content:center;padding:.7rem">
        Create Account
      </button>
    </form>

    <div class="auth-footer">
      Already have an account? <a href="${pageContext.request.contextPath}/login">Sign in</a>
    </div>
  </div>
</div>
</body>
</html>
