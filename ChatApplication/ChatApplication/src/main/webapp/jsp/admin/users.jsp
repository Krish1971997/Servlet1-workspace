<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ page import="com.chatapp.model.User"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Manage Users · Admin</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link
	href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap"
	rel="stylesheet">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<%@ include file="/jsp/common/navbar.jsp"%>

	<div class="container-wide"
		style="padding-top: 1.5rem; padding-bottom: 3rem">


		<div class="page-header">
			<div class="page-title">Manage Users</div>

			<a href="${pageContext.request.contextPath}/admin/dashboard"
				class="btn btn-ghost btn-sm"> ← Dashboard </a>
		</div>

		<div class="card">
			<div style="font-weight: 700; margin-bottom: .75rem">Create New
				User</div>
			<form method="post"
				action="${pageContext.request.contextPath}/admin/create">
				<input type="hidden" name="action" value="create">
				<!--<div style="display: grid; grid-template-columns: 1fr 2fr auto; gap: .75rem; align-items: end"> -->
				<div
					style="display: flex; gap: .75rem; align-items: end; flex-wrap: nowrap;">
					<div class="form-group" style="margin: 0">
						<label>Username</label> <input type="text" name="username"
							placeholder="e.g. General" required>
					</div>
					<div class="form-group" style="margin: 0">
						<label>Email</label> <input type="email" name="email"
							placeholder="you@example.com" required>
					</div>
					<div class="form-group" style="margin: 0">
						<label>Password</label> <input type="Password" name="Password"
							placeholder="Min 6 characters" required>
					</div>
					<div class="form-group" style="margin: 0">
						<label>Confirm Password</label> <input type="Password"
							name="confirmPassword" placeholder="Repeat password" required>
					</div>
					<div class="form-group" style="margin: 0">
						<label>Role</label> <select name="role">
							<option value="user">User</option>
							<option value="admin">Admin</option>
						</select>
					</div>
					<button type="submit" class="btn btn-primary">Create User</button>
				</div>

				<!-- <div class="form-group" style="margin: 0">
					<label>Username</label> <input type="text" name="username"
						placeholder="e.g. john_doe" required autofocus> <small
						style="color: var(--muted); font-size: .78rem"></small>
				</div>
				<div class="form-group" style="margin: 0">
					<label>Email address</label> <input type="email" name="email"
						placeholder="you@example.com" required>
				</div>
				<div class="form-group">
					<label>Password</label> <input type="password" name="password"
						placeholder="Min 6 characters" required>
				</div>
				<div class="form-group">
					<label>Confirm Password</label> <input type="password"
						name="confirmPassword" placeholder="Repeat password" required>
				</div>
				<button type="submit" class="btn btn-primary"
					style="width: 100%; justify-content: center; padding: .7rem">
					Create Account</button> -->
			</form>
		</div>

		<div class="card">
			<div class="table-wrap">
				<table>
					<thead>
						<tr>
							<th>#</th>
							<th>Username</th>
							<th>Email</th>
							<th>Status</th>
							<th>Joined</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="u" items="${users}">
							<tr>
								<td class="text-muted">${u.id}</td>
								<td style="font-weight: 600">${u.username}</td>
								<td class="text-muted">${u.email}</td>
								<td><c:choose>
										<c:when test="${u.active}">
											<span
												style="color: var(--success); font-size: .8rem; font-weight: 600">●
												Active</span>
										</c:when>
										<c:otherwise>
											<span
												style="color: var(--danger); font-size: .8rem; font-weight: 600">●
												Inactive</span>
										</c:otherwise>
									</c:choose></td>
								<td class="text-muted">${u.createdAt}</td>
								<td><c:choose>
										<c:when test="${u.active}">
											<form method="post"
												action="${pageContext.request.contextPath}/admin/users"
												onsubmit="return confirm('Deactivate ${u.username}?')"
												style="display: inline">
												<input type="hidden" name="action" value="deactivate">
												<input type="hidden" name="userId" value="${u.id}">
												<!-- <c:if
													test="${sessionScope.loggedUser.username != u.username}">
													<button class="btn btn-sm btn-danger">Deactivate</button>
												</c:if>  -->
												<button class="btn btn-sm btn-success">Deactivate</button>

											</form>
										</c:when>
										<c:otherwise>
											<form method="post"
												action="${pageContext.request.contextPath}/admin/users"
												style="display: inline">
												<input type="hidden" name="action" value="activate">
												<input type="hidden" name="userId" value="${u.id}">
												<button class="btn btn-sm btn-success">Activate</button>
											</form>
										</c:otherwise>
									</c:choose></td>
							</tr>
						</c:forEach>
						<c:if test="${empty users}">
							<tr>
								<td colspan="6" style="text-align: center; color: var(--muted)">No
									users found</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>
