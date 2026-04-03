<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Feed · ChatApp</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="/jsp/common/navbar.jsp" %>

<div class="container" style="padding-top:1.5rem;padding-bottom:3rem">

  <%-- ── New Post Box ──────────────────────────────────────── --%>
  <div class="card">
    <form method="post" action="${pageContext.request.contextPath}/feed">
      <div class="form-group" style="margin-bottom:.75rem">
        <textarea name="content" rows="3"
          placeholder="What's on your mind, ${sessionScope.loggedUser.username}?"></textarea>
      </div>
      <div style="display:flex;justify-content:flex-end">
        <button type="submit" class="btn btn-primary">Post</button>
      </div>
    </form>
  </div>

  <%-- ── Post List ────────────────────────────────────────── --%>
  <c:choose>
    <c:when test="${empty posts}">
      <div class="card" style="text-align:center;color:var(--muted);padding:2.5rem">
        No posts yet. Be the first to post!
      </div>
    </c:when>
    <c:otherwise>
      <c:forEach var="post" items="${posts}">
        <div class="card ${post.hidden ? 'post-hidden' : ''}">
          <%-- Header --%>
          <div class="card-header">
            <div class="flex">
              <div class="avatar">${fn:toUpperCase(fn:substring(post.authorUsername,0,1))}</div>
              <div>
                <div style="font-weight:600;font-size:.95rem">${post.authorUsername}</div>
                <div class="post-meta">
                  <span>${post.createdAt}</span>
                  <c:if test="${post.hidden}"><span class="hidden-badge">Hidden</span></c:if>
                  <c:if test="${post.shared}"><span class="shared-badge">Shared</span></c:if>
                </div>
              </div>
            </div>
            <%-- Admin actions --%>
            <c:if test="${sessionScope.loggedUser.admin}">
              <div class="flex">
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
                <form method="post" action="${pageContext.request.contextPath}/admin/posts">
                  <input type="hidden" name="action" value="delete">
                  <input type="hidden" name="postId" value="${post.id}">
                  <button class="btn btn-sm btn-danger" onclick="return confirm('Delete this post?')">Delete</button>
                </form>
              </div>
            </c:if>
          </div>

          <%-- Content --%>
          <div class="post-content">${post.content}</div>

          <%-- Comments --%>
          <div class="comments-section">
            <c:forEach var="comment" items="${post.comments}">
              <div class="comment-item ${comment.hidden ? 'comment-hidden' : ''}">
                <div class="avatar" style="width:28px;height:28px;font-size:.75rem">
                  ${fn:toUpperCase(fn:substring(comment.authorUsername,0,1))}
                </div>
                <div class="comment-body">
                  <div class="flex">
                    <span class="comment-author">${comment.authorUsername}</span>
                    <span class="comment-time">${comment.createdAt}</span>
                    <c:if test="${comment.hidden}"><span class="hidden-badge">Hidden</span></c:if>
                    <%-- Admin comment actions --%>
                    <c:if test="${sessionScope.loggedUser.admin}">
                      <c:choose>
                        <c:when test="${comment.hidden}">
                          <form method="post" action="${pageContext.request.contextPath}/admin/posts" style="display:inline">
                            <input type="hidden" name="action" value="unhideComment">
                            <input type="hidden" name="commentId" value="${comment.id}">
                            <button class="btn btn-link" style="font-size:.75rem;color:var(--success)">Unhide</button>
                          </form>
                        </c:when>
                        <c:otherwise>
                          <form method="post" action="${pageContext.request.contextPath}/admin/posts" style="display:inline">
                            <input type="hidden" name="action" value="hideComment">
                            <input type="hidden" name="commentId" value="${comment.id}">
                            <button class="btn btn-link" style="font-size:.75rem;color:var(--warn)">Hide</button>
                          </form>
                        </c:otherwise>
                      </c:choose>
                    </c:if>
                  </div>
                  <div class="comment-text">${comment.content}</div>
                </div>
              </div>
            </c:forEach>

            <%-- Add comment --%>
            <form method="post" action="${pageContext.request.contextPath}/comment" class="comment-form">
              <input type="hidden" name="postId" value="${post.id}">
              <input type="hidden" name="redirect" value="${pageContext.request.contextPath}/feed">
              <input type="text" name="content" placeholder="Write a comment…" required>
              <button type="submit" class="btn btn-primary btn-sm">Send</button>
            </form>
          </div>
        </div>
      </c:forEach>
    </c:otherwise>
  </c:choose>
</div>
</body>
</html>
