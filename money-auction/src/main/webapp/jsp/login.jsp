<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<title>Login</title>
<link rel="stylesheet" href="/css/style.css">
</head>
<body>
	<div class="container">
		<h2>Login</h2>
		<% if (request.getAttribute("error") != null) { %>
		<div class="alert alert-error"><%= request.getAttribute("error") %></div>
		<% } %>
		<form action="login" method="post">
			<div class="form-group">
				<label>Email:</label> <input type="email" name="email" required>
			</div>
			<div class="form-group">
				<label>Password:</label> <input type="password" name="password"
					required>
			</div>
			<button type="submit" value="Login">Login</button>
		</form>
	</div>
</body>
</html>
