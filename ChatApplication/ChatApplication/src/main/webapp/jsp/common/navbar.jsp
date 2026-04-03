<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="navbar">
  <a class="brand" href="${pageContext.request.contextPath}/feed">💬 ChatApp</a>
  <div class="nav-links">
    <a href="${pageContext.request.contextPath}/feed">Feed</a>
    <a href="${pageContext.request.contextPath}/groups">Groups</a>
    <c:if test="${sessionScope.loggedUser.admin}">
      <a href="${pageContext.request.contextPath}/admin/dashboard">Admin</a>
    </c:if>
  </div>
  <div class="nav-user">
    <div class="avatar">${sessionScope.loggedUser.username.substring(0,1).toUpperCase()}</div>
    <span class="username">${sessionScope.loggedUser.username}</span>
    <c:if test="${sessionScope.loggedUser.admin}"><span class="badge-admin">Admin</span></c:if>
    <a href="${pageContext.request.contextPath}/logout" style="color:var(--danger);font-size:.82rem;">Sign out</a>
  </div>
</nav>
