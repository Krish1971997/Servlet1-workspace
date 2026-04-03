<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Groups · ChatApp</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="/jsp/common/navbar.jsp" %>

<div class="container" style="padding-top:1.5rem;padding-bottom:3rem">

  <div class="page-header">
    <div class="page-title">Groups</div>
  </div>

  <%-- ── My Groups ─────────────────────────────────────────── --%>
  <div class="card">
    <div style="font-weight:700;margin-bottom:1rem">My Groups</div>
    <c:choose>
      <c:when test="${empty myGroups}">
        <p class="text-muted">You haven't joined any groups yet.</p>
      </c:when>
      <c:otherwise>
        <c:forEach var="g" items="${myGroups}">
          <div class="flex" style="padding:.5rem 0;border-bottom:1px solid var(--border)">
            <div style="flex:1">
              <a href="${pageContext.request.contextPath}/groups?id=${g.id}" style="font-weight:600">${g.name}</a>
              <div class="text-muted" style="font-size:.82rem">${g.description}</div>
            </div>
            <a href="${pageContext.request.contextPath}/groups?id=${g.id}" class="btn btn-ghost btn-sm">Open →</a>
          </div>
        </c:forEach>
      </c:otherwise>
    </c:choose>
  </div>

  <%-- ── Discover Groups ───────────────────────────────────── --%>
  <div class="card">
    <div style="font-weight:700;margin-bottom:1rem">Discover Groups</div>
    <c:forEach var="g" items="${allGroups}">
      <div class="flex" style="padding:.5rem 0;border-bottom:1px solid var(--border)">
        <div style="flex:1">
          <div style="font-weight:600">${g.name}</div>
          <div class="text-muted" style="font-size:.82rem">${g.description}</div>
          <div class="text-muted" style="font-size:.78rem">Created by ${g.createdByUsername}</div>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/groups">
          <input type="hidden" name="action" value="join">
          <input type="hidden" name="groupId" value="${g.id}">
          <button class="btn btn-primary btn-sm">Join</button>
        </form>
      </div>
    </c:forEach>
    <c:if test="${empty allGroups}">
      <p class="text-muted">No groups available.</p>
    </c:if>
  </div>
</div>
</body>
</html>
