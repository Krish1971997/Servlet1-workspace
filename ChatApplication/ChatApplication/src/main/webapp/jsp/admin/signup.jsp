<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Admin Registration · ChatApp</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link
	href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap"
	rel="stylesheet">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<div class="auth-wrap">
		<div class="auth-box">
			<div class="auth-title" style="color: var(--accent2)">⚙️ Admin
				Registration</div>
			<div class="auth-subtitle">Requires an admin secret key</div>

			<c:if test="${not empty error}">
				<div class="alert alert-error">${error}</div>
			</c:if>

			<form method="post"
				action="${pageContext.request.contextPath}/admin/signup" novalidate>
				<div class="form-group">
					<label>Admin Secret Key</label> <input type="password"
						name="adminSecret" placeholder="Contact your system admin"
						required autofocus>
				</div>
				<div class="form-group">
					<label>Username</label> <input type="text" name="username"
						placeholder="admin_username" required>
				</div>
				<div class="form-group">
					<label>Email address</label> <input type="email" name="email"
						placeholder="admin@example.com" required>
				</div>
				<div class="form-group">
					<label>Password</label> <input type="password" name="password"
						placeholder="Min 6 characters" required>
				</div>
				<div class="form-group">
					<label>Confirm Password</label> <input type="password"
						name="confirmPassword" required>
				</div>
				<button type="submit" class="btn btn-primary"
					style="width: 100%; justify-content: center; padding: .7rem">
					Create Admin Account</button>
			</form>

			<div class="auth-footer">
				<a href="${pageContext.request.contextPath}/admin/login">← Back
					to admin login</a>
			</div>
		</div>
	</div>
</body>
</html>
