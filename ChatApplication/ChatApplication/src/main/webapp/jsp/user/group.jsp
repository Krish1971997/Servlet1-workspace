<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${group.name} · ChatApp</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="/jsp/common/navbar.jsp" %>

<div class="container-wide" style="padding-top:1.5rem;padding-bottom:3rem">

  <div class="page-header">
    <div>
      <div class="page-title">${group.name}</div>
      <div class="text-muted" style="font-size:.85rem">${group.description}</div>
    </div>
    <div class="flex">
      <c:choose>
        <c:when test="${isMember}">
          <form method="post" action="${pageContext.request.contextPath}/groups">
            <input type="hidden" name="action" value="leave">
            <input type="hidden" name="groupId" value="${group.id}">
            <button class="btn btn-ghost btn-sm" onclick="return confirm('Leave group?')">Leave Group</button>
          </form>
        </c:when>
        <c:otherwise>
          <form method="post" action="${pageContext.request.contextPath}/groups">
            <input type="hidden" name="action" value="join">
            <input type="hidden" name="groupId" value="${group.id}">
            <button class="btn btn-primary btn-sm">Join Group</button>
          </form>
        </c:otherwise>
      </c:choose>
    </div>
  </div>

  <div class="two-col">

    <%-- ── LEFT: Group Chat ─────────────────────────────────── --%>
    <div>
      <div class="card" style="padding:0;overflow:hidden">
        <div style="padding:.75rem 1rem;border-bottom:1px solid var(--border);font-weight:700;font-size:.9rem">
          💬 Group Chat
        </div>
        <div class="chat-messages" id="chatMessages">
          <c:forEach var="msg" items="${messages}">
            <c:set var="mine" value="${msg.senderId == sessionScope.loggedUser.id}"/>
            <div class="chat-msg ${mine ? 'mine' : ''} ${msg.hidden ? 'hidden-msg' : ''}"
                 data-id="${msg.id}" id="msg-${msg.id}">
              <div class="avatar" style="width:28px;height:28px;font-size:.75rem;flex-shrink:0">
                ${fn:toUpperCase(fn:substring(msg.senderUsername,0,1))}
              </div>
              <div>
                <c:if test="${!mine}"><div class="chat-sender">${msg.senderUsername}</div></c:if>
                <div class="chat-bubble">
                  ${msg.message}
                  <c:if test="${msg.hidden}"><br><small style="opacity:.6">[hidden by admin]</small></c:if>
                </div>
                <div class="chat-time">${msg.createdAt}</div>
                <%-- Admin hide/unhide chat message --%>
                <c:if test="${sessionScope.loggedUser.admin}">
                  <c:choose>
                    <c:when test="${msg.hidden}">
                      <form method="post" action="${pageContext.request.contextPath}/admin/groups">
                        <input type="hidden" name="action" value="unhideMsg">
                        <input type="hidden" name="msgId" value="${msg.id}">
                        <button class="btn btn-link" style="font-size:.72rem;color:var(--success)">Unhide</button>
                      </form>
                    </c:when>
                    <c:otherwise>
                      <form method="post" action="${pageContext.request.contextPath}/admin/groups">
                        <input type="hidden" name="action" value="hideMsg">
                        <input type="hidden" name="msgId" value="${msg.id}">
                        <button class="btn btn-link" style="font-size:.72rem;color:var(--warn)">Hide</button>
                      </form>
                    </c:otherwise>
                  </c:choose>
                </c:if>
              </div>
            </div>
          </c:forEach>
        </div>

        <%-- Chat input --%>
        <c:if test="${isMember || sessionScope.loggedUser.admin}">
          <form method="post" action="${pageContext.request.contextPath}/groups"
                class="chat-input-bar" id="chatForm">
            <input type="hidden" name="action" value="chat">
            <input type="hidden" name="groupId" value="${group.id}">
            <input type="text" name="message" id="chatInput" placeholder="Type a message…" autocomplete="off">
            <button type="submit" class="btn btn-primary btn-sm">Send</button>
          </form>
        </c:if>
      </div>
    </div>

    <%-- ── RIGHT: Group Posts ────────────────────────────────── --%>
    <div>
      <%-- New post in group --%>
      <c:if test="${isMember || sessionScope.loggedUser.admin}">
        <div class="card">
          <form method="post" action="${pageContext.request.contextPath}/groups">
            <input type="hidden" name="action" value="post">
            <input type="hidden" name="groupId" value="${group.id}">
            <div class="form-group" style="margin-bottom:.75rem">
              <textarea name="content" rows="2" placeholder="Post something to this group…"></textarea>
            </div>
            <div style="display:flex;justify-content:flex-end">
              <button type="submit" class="btn btn-primary btn-sm">Post</button>
            </div>
          </form>
        </div>
      </c:if>

      <c:forEach var="post" items="${posts}">
        <div class="card">
          <div class="card-header">
            <div class="flex">
              <div class="avatar" style="width:30px;height:30px;font-size:.8rem">
                ${fn:toUpperCase(fn:substring(post.authorUsername,0,1))}
              </div>
              <div>
                <div style="font-weight:600;font-size:.9rem">${post.authorUsername}</div>
                <div class="post-meta">
                  <span>${post.createdAt}</span>
                  <c:if test="${post.hidden}"><span class="hidden-badge">Hidden</span></c:if>
                </div>
              </div>
            </div>
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
              </div>
            </c:if>
          </div>
          <div class="post-content">${post.content}</div>

          <div class="comments-section">
            <c:forEach var="comment" items="${post.comments}">
              <div class="comment-item">
                <div class="avatar" style="width:24px;height:24px;font-size:.7rem">
                  ${fn:toUpperCase(fn:substring(comment.authorUsername,0,1))}
                </div>
                <div class="comment-body">
                  <div class="flex">
                    <span class="comment-author">${comment.authorUsername}</span>
                    <span class="comment-time">${comment.createdAt}</span>
                    <c:if test="${comment.hidden}"><span class="hidden-badge">Hidden</span></c:if>
                  </div>
                  <div class="comment-text">${comment.content}</div>
                </div>
              </div>
            </c:forEach>
            <form method="post" action="${pageContext.request.contextPath}/comment" class="comment-form">
              <input type="hidden" name="postId" value="${post.id}">
              <input type="hidden" name="redirect" value="${pageContext.request.contextPath}/groups?id=${group.id}">
              <input type="text" name="content" placeholder="Comment…" required>
              <button type="submit" class="btn btn-primary btn-sm">→</button>
            </form>
          </div>
        </div>
      </c:forEach>
    </div>
  </div>
</div>

<script>
  // ── Real-time chat polling ──────────────────────────────────
  const chatMessages = document.getElementById('chatMessages');
  const groupId = ${group.id};
  const myUsername = '${sessionScope.loggedUser.username}';
  const isAdmin = ${sessionScope.loggedUser.admin};
  const ctx = '${pageContext.request.contextPath}';

  // Track last message ID for polling
  let lastId = 0;
  document.querySelectorAll('.chat-msg[data-id]').forEach(el => {
    const id = parseInt(el.dataset.id);
    if (id > lastId) lastId = id;
  });

  // Scroll to bottom initially
  chatMessages.scrollTop = chatMessages.scrollHeight;

  function buildMsgEl(msg) {
    const div = document.createElement('div');
    div.className = 'chat-msg' + (msg.mine ? ' mine' : '') + (msg.hidden ? ' hidden-msg' : '');
    div.dataset.id = msg.id;
    div.id = 'msg-' + msg.id;

    const initial = (msg.sender || '?')[0].toUpperCase();
    div.innerHTML = `
      <div class="avatar" style="width:28px;height:28px;font-size:.75rem;flex-shrink:0">${initial}</div>
      <div>
        ${!msg.mine ? `<div class="chat-sender">${msg.sender}</div>` : ''}
        <div class="chat-bubble">${msg.message}${msg.hidden ? '<br><small style="opacity:.6">[hidden by admin]</small>' : ''}</div>
        <div class="chat-time">${msg.time}</div>
      </div>`;
    return div;
  }

  async function poll() {
    try {
      const res = await fetch(`${ctx}/chat/poll?groupId=${groupId}&afterId=${lastId}`);
      if (!res.ok) return;
      const msgs = await res.json();
      if (msgs && msgs.length > 0) {
        const atBottom = chatMessages.scrollHeight - chatMessages.scrollTop - chatMessages.clientHeight < 60;
        msgs.forEach(m => {
          chatMessages.appendChild(buildMsgEl(m));
          if (m.id > lastId) lastId = m.id;
        });
        if (atBottom) chatMessages.scrollTop = chatMessages.scrollHeight;
      }
    } catch (e) { /* network error, retry next tick */ }
  }

  // Poll every 2 seconds
  setInterval(poll, 2000);

  // Submit chat via AJAX to avoid full page reload
  const chatForm = document.getElementById('chatForm');
  const chatInput = document.getElementById('chatInput');
  if (chatForm) {
    chatForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const msg = chatInput.value.trim();
      if (!msg) return;
      chatInput.value = '';
      chatInput.focus();
      const fd = new FormData(chatForm);
      fd.set('message', msg);
      await fetch(chatForm.action, { method: 'POST', body: fd });
      await poll();
    });
  }
</script>
</body>
</html>
