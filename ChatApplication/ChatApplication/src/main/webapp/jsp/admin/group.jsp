<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${group.name} · Admin</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="/jsp/common/navbar.jsp" %>

<div class="container-wide" style="padding-top:1.5rem;padding-bottom:3rem">

  <div class="page-header">
    <div>
      <div class="page-title">⚙️ ${group.name}</div>
      <div class="text-muted">${group.description}</div>
    </div>
    <a href="${pageContext.request.contextPath}/admin/groups" class="btn btn-ghost btn-sm">← All Groups</a>
  </div>

  <div style="display:grid;grid-template-columns:1fr 1fr;gap:1.5rem">

    <%-- ══════════════════════════════════════════════════════ --%>
    <%-- LEFT COLUMN                                           --%>
    <%-- ══════════════════════════════════════════════════════ --%>
    <div>

      <%-- ── Admin: Post in group (with optional share) ───── --%>
      <div class="card">
        <div style="font-weight:700;margin-bottom:.75rem">Post in Group</div>
        <form method="post" action="${pageContext.request.contextPath}/admin/groups">
          <input type="hidden" name="action" value="post">
          <input type="hidden" name="groupId" value="${group.id}">
          <div class="form-group">
            <label>Message</label>
            <textarea name="content" rows="3" placeholder="Write a post for this group…"></textarea>
          </div>
          <div class="form-group">
            <label>Share only with specific members (optional)</label>
            <select name="shareWith" multiple style="height:100px">
              <c:forEach var="u" items="${allUsers}">
                <c:if test="${members.contains(u.id)}">
                  <option value="${u.id}">${u.username}</option>
                </c:if>
              </c:forEach>
            </select>
            <small class="text-muted" style="font-size:.75rem">
              Hold Ctrl/Cmd to select multiple. Leave empty = visible to all members.
            </small>
          </div>
          <button type="submit" class="btn btn-primary btn-sm">Post</button>
        </form>
      </div>

      <%-- ── Group Posts ─────────────────────────────────── --%>
      <div class="card">
        <div style="font-weight:700;margin-bottom:.75rem">Group Posts</div>
        <c:choose>
          <c:when test="${empty posts}">
            <p class="text-muted">No posts in this group.</p>
          </c:when>
          <c:otherwise>
            <c:forEach var="post" items="${posts}">
              <div style="padding:.75rem 0;border-bottom:1px solid var(--border)">
                <div class="flex">
                  <div class="avatar" style="width:28px;height:28px;font-size:.75rem">
                    ${fn:toUpperCase(fn:substring(post.authorUsername,0,1))}
                  </div>
                  <div style="flex:1">
                    <div style="font-size:.85rem;font-weight:600">${post.authorUsername}
                      <span class="text-muted" style="font-weight:400">· ${post.createdAt}</span>
                      <c:if test="${post.hidden}"><span class="hidden-badge" style="margin-left:.5rem">Hidden</span></c:if>
                    </div>
                    <div style="font-size:.875rem;margin:.25rem 0">${fn:substring(post.content,0,150)}${fn:length(post.content)>150?'…':''}</div>
                  </div>
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
                    <form method="post" action="${pageContext.request.contextPath}/admin/posts"
                          onsubmit="return confirm('Delete post?')">
                      <input type="hidden" name="action" value="delete">
                      <input type="hidden" name="postId" value="${post.id}">
                      <button class="btn btn-sm btn-danger">Del</button>
                    </form>
                  </div>
                </div>

                <%-- Comments under post --%>
                <c:if test="${not empty post.comments}">
                  <div style="margin-left:2rem;margin-top:.5rem">
                    <c:forEach var="comment" items="${post.comments}">
                      <div class="flex" style="padding:.3rem 0;border-top:1px solid var(--border);flex-wrap:wrap;gap:.35rem">
                        <span style="font-size:.8rem;font-weight:600;color:var(--accent2)">${comment.authorUsername}</span>
                        <span style="font-size:.8rem;flex:1">${comment.content}</span>
                        <c:if test="${comment.hidden}"><span class="hidden-badge">Hidden</span></c:if>
                        <c:choose>
                          <c:when test="${comment.hidden}">
                            <form method="post" action="${pageContext.request.contextPath}/admin/posts">
                              <input type="hidden" name="action" value="unhideComment">
                              <input type="hidden" name="commentId" value="${comment.id}">
                              <button class="btn btn-link" style="font-size:.75rem;color:var(--success)">Unhide</button>
                            </form>
                          </c:when>
                          <c:otherwise>
                            <form method="post" action="${pageContext.request.contextPath}/admin/posts">
                              <input type="hidden" name="action" value="hideComment">
                              <input type="hidden" name="commentId" value="${comment.id}">
                              <button class="btn btn-link" style="font-size:.75rem;color:var(--warn)">Hide</button>
                            </form>
                          </c:otherwise>
                        </c:choose>
                        <form method="post" action="${pageContext.request.contextPath}/admin/posts">
                          <input type="hidden" name="action" value="deleteComment">
                          <input type="hidden" name="commentId" value="${comment.id}">
                          <button class="btn btn-link" style="font-size:.75rem;color:var(--danger)">Del</button>
                        </form>
                      </div>
                    </c:forEach>
                  </div>
                </c:if>
              </div>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </div>
    </div>

    <%-- ══════════════════════════════════════════════════════ --%>
    <%-- RIGHT COLUMN                                          --%>
    <%-- ══════════════════════════════════════════════════════ --%>
    <div>

      <%-- ── Chat Messages (with hide/unhide) ──────────────── --%>
      <div class="card" style="padding:0;overflow:hidden">
        <div style="padding:.75rem 1rem;border-bottom:1px solid var(--border);font-weight:700">
          Chat Messages
          <span class="text-muted" style="font-weight:400;font-size:.8rem">(admin view — all messages)</span>
        </div>
        <div style="max-height:400px;overflow-y:auto;padding:.75rem">
          <c:choose>
            <c:when test="${empty messages}">
              <p class="text-muted" style="text-align:center;padding:1rem">No messages yet</p>
            </c:when>
            <c:otherwise>
              <c:forEach var="msg" items="${messages}">
                <div class="flex" style="padding:.4rem 0;border-bottom:1px solid var(--border);flex-wrap:wrap;gap:.35rem;align-items:flex-start">
                  <div class="avatar" style="width:26px;height:26px;font-size:.7rem;flex-shrink:0">
                    ${fn:toUpperCase(fn:substring(msg.senderUsername,0,1))}
                  </div>
                  <div style="flex:1;min-width:0">
                    <div style="font-size:.8rem">
                      <span style="font-weight:600;color:var(--accent2)">${msg.senderUsername}</span>
                      <span class="text-muted">${msg.createdAt}</span>
                      <c:if test="${msg.hidden}"><span class="hidden-badge" style="margin-left:.25rem">Hidden</span></c:if>
                    </div>
                    <div style="font-size:.875rem;${msg.hidden ? 'opacity:.5;font-style:italic' : ''}">${msg.message}</div>
                  </div>
                  <div class="flex" style="flex-shrink:0">
                    <c:choose>
                      <c:when test="${msg.hidden}">
                        <form method="post" action="${pageContext.request.contextPath}/admin/groups">
                          <input type="hidden" name="action" value="unhideMsg">
                          <input type="hidden" name="msgId" value="${msg.id}">
                          <button class="btn btn-link" style="font-size:.75rem;color:var(--success)">Unhide</button>
                        </form>
                      </c:when>
                      <c:otherwise>
                        <form method="post" action="${pageContext.request.contextPath}/admin/groups">
                          <input type="hidden" name="action" value="hideMsg">
                          <input type="hidden" name="msgId" value="${msg.id}">
                          <button class="btn btn-link" style="font-size:.75rem;color:var(--warn)">Hide</button>
                        </form>
                      </c:otherwise>
                    </c:choose>
                  </div>
                </div>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </div>
      </div>

      <%-- ── Member Management ───────────────────────────── --%>
      <div class="card">
        <div style="font-weight:700;margin-bottom:.75rem">Member Management</div>

        <%-- Add member --%>
        <form method="post" action="${pageContext.request.contextPath}/admin/groups"
              class="flex" style="margin-bottom:1rem">
          <input type="hidden" name="action" value="addMember">
          <input type="hidden" name="groupId" value="${group.id}">
          <select name="userId" style="flex:1">
            <option value="">-- Select user to add --</option>
            <c:forEach var="u" items="${allUsers}">
              <c:if test="${!members.contains(u.id)}">
                <option value="${u.id}">${u.username} (${u.email})</option>
              </c:if>
            </c:forEach>
          </select>
          <button type="submit" class="btn btn-success btn-sm">Add</button>
        </form>

        <%-- Current members --%>
        <div style="font-size:.8rem;color:var(--muted);margin-bottom:.5rem;text-transform:uppercase;letter-spacing:.5px">
          Current Members (${fn:length(members)})
        </div>
        <c:forEach var="u" items="${allUsers}">
          <c:if test="${members.contains(u.id)}">
            <div class="flex" style="padding:.4rem 0;border-bottom:1px solid var(--border)">
              <div class="avatar" style="width:26px;height:26px;font-size:.7rem">
                ${fn:toUpperCase(fn:substring(u.username,0,1))}
              </div>
              <span style="flex:1;font-size:.875rem">${u.username}</span>
              <form method="post" action="${pageContext.request.contextPath}/admin/groups">
                <input type="hidden" name="action" value="removeMember">
                <input type="hidden" name="groupId" value="${group.id}">
                <input type="hidden" name="userId" value="${u.id}">
                <button class="btn btn-link" style="font-size:.75rem;color:var(--danger)"
                        onclick="return confirm('Remove ${u.username}?')">Remove</button>
              </form>
            </div>
          </c:if>
        </c:forEach>
      </div>

      <%-- ── Share a post with specific users info ──────── --%>
      <div class="card" style="background:rgba(91,99,248,.07);border-color:rgba(91,99,248,.2)">
        <div style="font-size:.8rem;font-weight:700;color:var(--accent2);margin-bottom:.4rem">
          ℹ️ Selective Post Sharing
        </div>
        <div style="font-size:.82rem;color:var(--muted);line-height:1.6">
          When you post in this group and select specific members in the "Share only with" 
          dropdown, only those selected users will see that post — similar to Slack's 
          direct message in a channel or Teams' targeted announcements. Other group members 
          won't see it. Leave the selection empty to share with everyone.
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
