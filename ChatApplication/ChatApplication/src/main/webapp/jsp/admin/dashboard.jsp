<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Admin Dashboard · ChatApp</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="/jsp/common/navbar.jsp" %>

<div class="container-wide" style="padding-top:1.5rem;padding-bottom:3rem">

  <div class="page-header">
    <div class="page-title">⚙️ Admin Dashboard</div>
    <div class="flex">
      <a href="${pageContext.request.contextPath}/admin/users"  class="btn btn-ghost btn-sm">Manage Users</a>
      <a href="${pageContext.request.contextPath}/admin/groups" class="btn btn-ghost btn-sm">Manage Groups</a>
      <a href="${pageContext.request.contextPath}/feed"         class="btn btn-primary btn-sm">Go to Feed</a>
    </div>
  </div>

  <%-- ── Stats row ────────────────────────────────────────── --%>
  <div style="display:grid;grid-template-columns:repeat(3,1fr);gap:1rem;margin-bottom:1.5rem">
    <div class="card" style="text-align:center">
      <div style="font-size:2rem;font-weight:700;color:var(--accent2)">${fn:length(users)}</div>
      <div class="text-muted">Total Users</div>
    </div>
    <div class="card" style="text-align:center">
      <div style="font-size:2rem;font-weight:700;color:var(--accent2)">${fn:length(posts)}</div>
      <div class="text-muted">Wall Posts</div>
    </div>
    <div class="card" style="text-align:center">
      <div style="font-size:2rem;font-weight:700;color:var(--accent2)">${fn:length(groups)}</div>
      <div class="text-muted">Groups</div>
    </div>
  </div>

  <%-- ── Quick post as admin ─────────────────────────────── --%>
  <div class="card">
    <div style="font-weight:700;margin-bottom:.75rem">Post as Admin</div>
    <form method="post" action="${pageContext.request.contextPath}/admin/posts">
      <input type="hidden" name="action" value="create">
      <div class="flex" style="margin-bottom:.75rem;align-items:flex-start;gap:.75rem">
        <textarea name="content" rows="2" placeholder="Write a post…" style="flex:1"></textarea>
        <div>
          <label class="form-group" style="margin:0">
            <select name="groupId" style="min-width:160px">
              <option value="">Wall (public)</option>
              <c:forEach var="g" items="${groups}">
                <option value="${g.id}">${g.name}</option>
              </c:forEach>
            </select>
          </label>
        </div>
      </div>
      <button type="submit" class="btn btn-primary btn-sm">Publish</button>
    </form>
  </div>

  <%-- ── Recent posts with moderation ─────────────────────── --%>
  <div class="card">
    <div style="font-weight:700;margin-bottom:.75rem">Recent Wall Posts</div>
    <c:forEach var="post" items="${posts}" varStatus="s">
      <c:if test="${s.index < 10}">
        <div style="padding:.75rem 0;border-bottom:1px solid var(--border)">
          <div class="flex">
            <div class="avatar" style="width:30px;height:30px;font-size:.8rem">
              ${fn:toUpperCase(fn:substring(post.authorUsername,0,1))}
            </div>
            <div style="flex:1">
              <div style="font-size:.85rem;font-weight:600">${post.authorUsername}
                <span class="text-muted" style="font-weight:400">· ${post.createdAt}</span>
              </div>
              <div style="font-size:.875rem;margin-top:.2rem">${fn:substring(post.content,0,120)}${fn:length(post.content) > 120 ? '…' : ''}</div>
            </div>
            <div class="flex">
              <c:if test="${post.hidden}"><span class="hidden-badge">Hidden</span></c:if>
              <c:choose>
                <c:when test="${post.hidden}">
                  <form method="post" action="${pageContext.request.contextPath}/admin/posts">
                    <input type="hidden" name="action" value="unhide">
                    <input type="hidden" name="postId" value="${post.id}">
                    <button class="btn btn-sm btn-success">Unhide</button>
                  </form>
                </c:when>
                <c:otherwise>
                  <form method="post" action="${pageContext.request.contextPath}/admin/posts">
                    <input type="hidden" name="action" value="hide">
                    <input type="hidden" name="postId" value="${post.id}">
                    <button class="btn btn-sm btn-warn">Hide</button>
                  </form>
                </c:otherwise>
              </c:choose>
              <form method="post" action="${pageContext.request.contextPath}/admin/posts"
                    onsubmit="return confirm('Delete this post?')">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="postId" value="${post.id}">
                <button class="btn btn-sm btn-danger">Delete</button>
              </form>
            </div>
          </div>
        </div>
      </c:if>
    </c:forEach>
  </div>

  <%-- ── Groups summary ─────────────────────────────────────── --%>
  <div class="card">
    <div class="flex" style="margin-bottom:.75rem">
      <div style="font-weight:700">Groups</div>
      <div class="spacer"></div>
      <a href="${pageContext.request.contextPath}/admin/groups" class="btn btn-ghost btn-sm">View All →</a>
    </div>
    <div class="table-wrap">
      <table>
        <thead><tr><th>Name</th><th>Description</th><th>Created By</th><th>Actions</th></tr></thead>
        <tbody>
          <c:forEach var="g" items="${groups}">
            <tr>
              <td><a href="${pageContext.request.contextPath}/admin/groups?id=${g.id}">${g.name}</a></td>
              <td class="text-muted">${g.description}</td>
              <td>${g.createdByUsername}</td>
              <td><a href="${pageContext.request.contextPath}/admin/groups?id=${g.id}" class="btn btn-ghost btn-sm">Manage</a></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</div>
</body>
</html>
