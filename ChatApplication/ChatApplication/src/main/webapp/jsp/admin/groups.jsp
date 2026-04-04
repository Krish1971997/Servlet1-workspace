<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Manage Groups · Admin</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="/jsp/common/navbar.jsp" %>

<div class="container-wide" style="padding-top:1.5rem;padding-bottom:3rem">

  <div class="page-header">
    <div class="page-title">Manage Groups</div>
    <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-ghost btn-sm">← Dashboard</a>
  </div>

  <%-- ── Create Group ──────────────────────────────────────── --%>
  <div class="card">
    <div style="font-weight:700;margin-bottom:.75rem">Create New Group</div>
    <form method="post" action="${pageContext.request.contextPath}/admin/groups">
      <input type="hidden" name="action" value="create">
      <div style="display:grid;grid-template-columns:1fr 2fr auto;gap:.75rem;align-items:end">
        <div class="form-group" style="margin:0">
          <label>Group Name</label>
          <input type="text" name="name" placeholder="e.g. General" required>
        </div>
        <div class="form-group" style="margin:0">
          <label>Description</label>
          <input type="text" name="description" placeholder="Short description">
        </div>
        <button type="submit" class="btn btn-primary">Create New Group
</button>
      </div>
    </form>
  </div>

  <%-- ── Groups Table ──────────────────────────────────────── --%>
  <div class="card">
    <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>Name</th>
            <th>Description</th>
            <th>Created By</th>
            <th>Created</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="g" items="${groups}">
            <tr>
              <td class="text-muted">${g.id}</td>
              <td style="font-weight:600">${g.name}</td>
              <td class="text-muted">${g.description}</td>
              <td>${g.createdByUsername}</td>
              <td class="text-muted">${g.createdAt}</td>
              <td>
                <a href="${pageContext.request.contextPath}/admin/groups?id=${g.id}"
                   class="btn btn-ghost btn-sm">Manage →</a>
              </td>
            </tr>
          </c:forEach>
          <c:if test="${empty groups}">
            <tr><td colspan="6" style="text-align:center;color:var(--muted);padding:2rem">No groups yet</td></tr>
          </c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>
</body>
</html>
