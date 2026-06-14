<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1.0">
<title>ExpenseIQ – Bill Reminders</title>
<link href="https://fonts.googleapis.com/css2?family=Syne:wght@400;600;700;800&family=DM+Sans:wght@300;400;500&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<style>
/* ── Hero ── */
.reminder-hero{background:linear-gradient(135deg,#1a1a2e,#0f3460);border-radius:20px;padding:32px 40px;margin-bottom:24px;display:flex;align-items:center;justify-content:space-between;gap:20px;overflow:hidden;position:relative}
.reminder-hero::before{content:'';position:absolute;width:280px;height:280px;background:radial-gradient(circle,rgba(245,158,11,.2),transparent 70%);top:-80px;right:-40px;border-radius:50%}
.hero-text h2{font-family:'Syne',sans-serif;font-size:1.7rem;font-weight:800;color:#fff;margin:0 0 5px}
.hero-text p{color:rgba(255,255,255,.6);font-size:.9rem;margin:0}

/* ── SSE status dot ── */
.sse-status{display:inline-flex;align-items:center;gap:6px;font-size:.75rem;padding:4px 10px;border-radius:20px;font-weight:600}
.sse-live{background:rgba(16,185,129,.15);color:#10b981;border:1px solid rgba(16,185,129,.3)}
.sse-offline{background:rgba(239,68,68,.12);color:#f87171;border:1px solid rgba(239,68,68,.2)}
.sse-dot{width:7px;height:7px;border-radius:50%}
.sse-live .sse-dot{background:#10b981;animation:pulse-dot 1.5s ease-in-out infinite}
.sse-offline .sse-dot{background:#f87171}
@keyframes pulse-dot{0%,100%{opacity:1;transform:scale(1)}50%{opacity:.5;transform:scale(.7)}}

/* ── Stats ── */
.stats-row{display:grid;grid-template-columns:repeat(4,1fr);gap:14px;margin-bottom:24px}
.stat-card{background:var(--card-bg);border:1px solid var(--border);border-radius:14px;padding:18px 20px;display:flex;align-items:center;gap:12px}
.stat-icon{width:42px;height:42px;border-radius:11px;display:flex;align-items:center;justify-content:center;font-size:1.15rem;flex-shrink:0}
.si-red{background:rgba(239,68,68,.15);color:#ef4444}
.si-amber{background:rgba(245,158,11,.15);color:#f59e0b}
.si-purple{background:rgba(99,102,241,.15);color:#818cf8}
.si-green{background:rgba(16,185,129,.15);color:#10b981}
.stat-info label{font-size:.72rem;color:var(--text-muted);text-transform:uppercase;letter-spacing:.05em}
.stat-info span{display:block;font-size:1.3rem;font-weight:700;font-family:'Syne',sans-serif;color:var(--text-primary)}

/* ── Cards ── */
.reminders-grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(300px,1fr));gap:16px;margin-bottom:28px}
.reminder-card{background:var(--card-bg);border:1px solid var(--border);border-radius:16px;padding:20px;transition:.2s;position:relative;overflow:hidden}
.reminder-card:hover{transform:translateY(-2px);border-color:rgba(99,102,241,.3)}
.reminder-card.inactive{opacity:.45}
.reminder-card.highlight{border-color:#f59e0b;box-shadow:0 0 20px rgba(245,158,11,.2);animation:highlight-flash .6s ease}
@keyframes highlight-flash{0%{background:rgba(245,158,11,.08)}100%{background:var(--card-bg)}}

.card-header{display:flex;align-items:flex-start;justify-content:space-between;margin-bottom:14px}
.card-icon{width:40px;height:40px;border-radius:10px;display:flex;align-items:center;justify-content:center;font-size:1.1rem;flex-shrink:0}
.card-actions{display:flex;gap:6px}
.act-btn{width:28px;height:28px;border-radius:7px;border:none;cursor:pointer;display:flex;align-items:center;justify-content:center;font-size:.75rem;transition:.15s;text-decoration:none}
.act-btn:hover{transform:translateY(-1px)}
.ab-edit{background:rgba(99,102,241,.12);color:#818cf8}.ab-edit:hover{background:rgba(99,102,241,.25)}
.ab-paid{background:rgba(16,185,129,.12);color:#10b981}.ab-paid:hover{background:rgba(16,185,129,.25)}
.ab-del{background:rgba(239,68,68,.1);color:#f87171}.ab-del:hover{background:rgba(239,68,68,.25)}
.ab-toggle{background:rgba(148,163,184,.1);color:#94a3b8}.ab-toggle:hover{background:rgba(148,163,184,.2)}
.card-title{font-family:'Syne',sans-serif;font-size:1rem;font-weight:700;margin-bottom:2px}
.card-note{font-size:.78rem;color:var(--text-muted)}
.card-amount{font-family:'Syne',sans-serif;font-size:1.5rem;font-weight:800;color:var(--text-primary);margin:10px 0 6px}
.card-meta{display:flex;align-items:center;gap:8px;flex-wrap:wrap;margin-bottom:12px}
.freq-badge{font-size:.7rem;padding:2px 8px;border-radius:6px;font-weight:600;text-transform:uppercase;background:rgba(99,102,241,.1);color:#818cf8}
.cat-badge{font-size:.7rem;padding:2px 8px;border-radius:6px;background:rgba(148,163,184,.1);color:#94a3b8}
.auto-badge{font-size:.7rem;padding:2px 8px;border-radius:6px;background:rgba(16,185,129,.1);color:#10b981}

/* ── Status chips ── */
.status-chip{display:inline-flex;align-items:center;gap:5px;padding:4px 10px;border-radius:20px;font-size:.75rem;font-weight:600}
.status-overdue{background:rgba(239,68,68,.15);color:#ef4444;border:1px solid rgba(239,68,68,.3)}
.status-today{background:rgba(245,158,11,.15);color:#f59e0b;border:1px solid rgba(245,158,11,.3);animation:pulse-chip 1.5s ease-in-out infinite}
.status-soon{background:rgba(245,158,11,.1);color:#fbbf24;border:1px solid rgba(245,158,11,.2)}
.status-ok{background:rgba(16,185,129,.1);color:#10b981;border:1px solid rgba(16,185,129,.2)}
@keyframes pulse-chip{0%,100%{opacity:1}50%{opacity:.65}}
.due-bar-wrap{background:rgba(255,255,255,.06);border-radius:4px;height:4px;margin-top:12px}
.due-bar-fill{height:4px;border-radius:4px;transition:width .4s}

/* ── TOAST SYSTEM ── */
.notif-tray{position:fixed;top:72px;right:20px;z-index:9999;display:flex;flex-direction:column;gap:10px;max-width:360px;pointer-events:none}
.toast{background:#1c1c2e;border:1px solid var(--border);border-radius:14px;padding:0;overflow:hidden;box-shadow:0 12px 40px rgba(0,0,0,.5);animation:toastIn .35s cubic-bezier(.34,1.56,.64,1);pointer-events:all;display:flex;min-width:300px}
.toast-accent{width:4px;flex-shrink:0}
.toast-accent.bill_due{background:#f59e0b}
.toast-accent.due_today{background:#ef4444;animation:flash-bar 1s ease-in-out infinite}
.toast-accent.overdue{background:#ef4444}
.toast-accent.auto_expense{background:#10b981}
.toast-accent.paid{background:#6366f1}
@keyframes flash-bar{0%,100%{opacity:1}50%{opacity:.3}}
.toast-body{padding:14px 14px 14px 12px;flex:1}
.toast-header{display:flex;align-items:center;justify-content:space-between;margin-bottom:4px}
.toast-title{font-family:'Syne',sans-serif;font-size:.9rem;font-weight:700;color:#f0f0fa}
.toast-close{background:none;border:none;cursor:pointer;color:#5a5e78;font-size:.85rem;padding:0;line-height:1;transition:.15s}
.toast-close:hover{color:#f0f0fa}
.toast-msg{font-size:.8rem;color:#a9adc7;line-height:1.5}
.toast-time{font-size:.7rem;color:#5a5e78;margin-top:4px}
.toast-actions{display:flex;gap:6px;margin-top:8px}
.ta-btn{font-size:.72rem;padding:3px 10px;border-radius:6px;border:none;cursor:pointer;font-family:inherit;font-weight:600;transition:.15s}
.ta-view{background:rgba(99,102,241,.15);color:#818cf8}.ta-view:hover{background:rgba(99,102,241,.3)}
.ta-dismiss{background:rgba(148,163,184,.1);color:#94a3b8}.ta-dismiss:hover{background:rgba(148,163,184,.2)}
@keyframes toastIn{from{transform:translateX(110%) scale(.9);opacity:0}to{transform:translateX(0) scale(1);opacity:1}}
@keyframes toastOut{from{transform:translateX(0);opacity:1;max-height:200px}to{transform:translateX(110%);opacity:0;max-height:0;margin:0;padding:0}}

/* ── Bell badge ── */
.bell-wrap{position:relative;display:inline-flex;align-items:center;cursor:pointer}
.bell-badge{position:absolute;top:-5px;right:-5px;background:#ef4444;color:#fff;border-radius:50%;width:17px;height:17px;font-size:.6rem;display:flex;align-items:center;justify-content:center;font-weight:700;border:2px solid var(--surface)}
.bell-shake{animation:bell-shake .5s ease}
@keyframes bell-shake{0%,100%{transform:rotate(0)}20%{transform:rotate(-15deg)}40%{transform:rotate(15deg)}60%{transform:rotate(-10deg)}80%{transform:rotate(10deg)}}

/* ── Notification log ── */
.notif-item{display:flex;align-items:center;gap:12px;padding:10px 0;border-bottom:1px solid var(--border)}
.notif-item:last-child{border-bottom:none}
.notif-dot{width:8px;height:8px;border-radius:50%;background:#f59e0b;flex-shrink:0}
.notif-dot.overdue{background:#ef4444}
.notif-text{font-size:.84rem;color:var(--text-secondary);flex:1}
.notif-time{font-size:.75rem;color:var(--text-muted);white-space:nowrap}

/* ── Notification Panel (slide-in) ── */
.notif-panel{position:fixed;top:0;right:-400px;width:380px;height:100vh;background:var(--surface);border-left:1px solid var(--border);z-index:2000;transition:right .3s ease;display:flex;flex-direction:column;box-shadow:-10px 0 40px rgba(0,0,0,.4)}
.notif-panel.open{right:0}
.np-header{padding:20px 20px 16px;border-bottom:1px solid var(--border);display:flex;align-items:center;justify-content:space-between}
.np-header h3{font-family:'Syne',sans-serif;font-size:1rem;font-weight:700;margin:0}
.np-close{background:none;border:none;cursor:pointer;color:var(--text-muted);font-size:1.1rem}
.np-close:hover{color:var(--text-primary)}
.np-body{flex:1;overflow-y:auto;padding:12px 16px}
.np-item{background:var(--card-bg);border:1px solid var(--border);border-radius:12px;padding:12px 14px;margin-bottom:10px}
.np-item-title{font-size:.88rem;font-weight:600;color:var(--text-primary);margin-bottom:3px}
.np-item-sub{font-size:.78rem;color:var(--text-muted);line-height:1.5}
.np-empty{text-align:center;padding:40px 20px;color:var(--text-muted);font-size:.85rem}
.np-footer{padding:14px 16px;border-top:1px solid var(--border)}

/* ── Modal ── */
.modal-overlay{display:none;position:fixed;inset:0;z-index:1000;background:rgba(0,0,0,.65);backdrop-filter:blur(4px);align-items:center;justify-content:center}
.modal-overlay.open{display:flex}
.modal-box{background:var(--card-bg);border:1px solid var(--border);border-radius:18px;width:100%;max-width:400px;padding:28px;animation:slideUp .22s ease}
@keyframes slideUp{from{transform:translateY(16px);opacity:0}to{transform:translateY(0);opacity:1}}
.modal-box h3{font-family:'Syne',sans-serif;font-size:1.1rem;font-weight:700;margin-bottom:8px}
.modal-box p{color:var(--text-muted);font-size:.85rem;margin-bottom:18px;line-height:1.6}
.modal-actions{display:flex;gap:10px;justify-content:flex-end}

@media(max-width:900px){.stats-row{grid-template-columns:repeat(2,1fr)}.reminders-grid{grid-template-columns:1fr}.notif-panel{width:100%}}
</style>
</head>
<body>

<nav class="sidebar">
<div class="sidebar-brand"><span class="brand-icon">💰</span><span class="brand-name">ExpenseIQ</span></div>
<ul class="nav-links">
  <li><a href="${pageContext.request.contextPath}/dashboard"><i class="fas fa-chart-pie"></i><span>Dashboard</span></a></li>
  <li><a href="${pageContext.request.contextPath}/income/list"><i class="fas fa-arrow-trend-up"></i><span>Income</span></a></li>
  <li><a href="${pageContext.request.contextPath}/expense/list"><i class="fas fa-arrow-trend-down"></i><span>Expenses</span></a></li>
  <li><a href="${pageContext.request.contextPath}/budget/list"><i class="fas fa-wallet"></i><span>Budget</span></a></li>
  <li><a href="${pageContext.request.contextPath}/budget/trends"><i class="fas fa-chart-line"></i><span>Trends</span></a></li>
  <li><a href="${pageContext.request.contextPath}/reports"><i class="fas fa-chart-bar"></i><span>Reports</span></a></li>
  <li><a href="${pageContext.request.contextPath}/reminders/list" class="active"><i class="fas fa-bell"></i><span>Reminders</span></a></li>
  <li><a href="${pageContext.request.contextPath}/backup/list"><i class="fas fa-database"></i><span>Backup</span></a></li>
</ul>
<div class="sidebar-footer"><span>v1.0.0</span></div>
</nav>

<main class="main-content">
<div class="topbar">
  <div class="page-title">Bill Reminders</div>
  <div class="topbar-actions" style="gap:12px">
    <!-- SSE Connection status -->
    <span class="sse-status sse-offline" id="sseStatus">
      <span class="sse-dot"></span>
      <span id="sseLabel">Connecting…</span>
    </span>
    <!-- Bell with badge -->
    <div class="bell-wrap" id="bellWrap" onclick="openPanel()" title="Notifications">
      <i class="fas fa-bell" style="font-size:1.1rem;color:var(--text-secondary)" id="bellIcon"></i>
      <span class="bell-badge" id="bellBadge" style="display:none">0</span>
    </div>
    <a href="${pageContext.request.contextPath}/reminders/add" class="btn btn-primary btn-sm">
      <i class="fas fa-plus"></i> Add Reminder
    </a>
  </div>
</div>

<c:if test="${not empty sessionScope.successMsg}">
  <div class="alert alert-success" style="margin:14px 28px 0"><i class="fas fa-check-circle"></i> ${sessionScope.successMsg}</div>
  <c:remove var="successMsg" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.errorMsg}">
  <div class="alert alert-danger" style="margin:14px 28px 0"><i class="fas fa-exclamation-circle"></i> ${sessionScope.errorMsg}</div>
  <c:remove var="errorMsg" scope="session"/>
</c:if>

<div class="page-body">

  <!-- Hero -->
  <div class="reminder-hero">
    <div class="hero-text">
      <h2><i class="fas fa-bolt" style="color:#f59e0b"></i> &nbsp;Real-Time Bill Alerts</h2>
      <p>SSE-powered push notifications — EB bill, recharge, credit card, EMI.<br>
         Get alerted the moment a bill is due. No page refresh needed.</p>
    </div>
    <a href="${pageContext.request.contextPath}/reminders/add" class="btn btn-primary">
      <i class="fas fa-plus"></i> Add Bill
    </a>
  </div>

  <!-- Stats -->
  <c:set var="overdueCount"  value="0"/>
  <c:set var="dueTodayCount" value="0"/>
  <c:set var="dueSoonCount"  value="0"/>
  <c:set var="activeCount"   value="0"/>
  <c:forEach var="r" items="${reminders}">
    <c:if test="${r.active}">           <c:set var="activeCount"   value="${activeCount+1}"/></c:if>
    <c:if test="${r.overdue}">          <c:set var="overdueCount"  value="${overdueCount+1}"/></c:if>
    <c:if test="${r.dueToday}">         <c:set var="dueTodayCount" value="${dueTodayCount+1}"/></c:if>
    <c:if test="${r.dueSoon && !r.dueToday && !r.overdue}">
                                        <c:set var="dueSoonCount"  value="${dueSoonCount+1}"/></c:if>
  </c:forEach>

  <div class="stats-row">
    <div class="stat-card">
      <div class="stat-icon si-red"><i class="fas fa-circle-exclamation"></i></div>
      <div class="stat-info"><label>Overdue</label><span>${overdueCount}</span></div>
    </div>
    <div class="stat-card">
      <div class="stat-icon si-amber"><i class="fas fa-clock"></i></div>
      <div class="stat-info"><label>Due Today</label><span>${dueTodayCount}</span></div>
    </div>
    <div class="stat-card">
      <div class="stat-icon si-purple"><i class="fas fa-hourglass-half"></i></div>
      <div class="stat-info"><label>Due Soon</label><span>${dueSoonCount}</span></div>
    </div>
    <div class="stat-card">
      <div class="stat-icon si-green"><i class="fas fa-wifi"></i></div>
      <div class="stat-info"><label>Live Tabs</label><span id="sseCount">${sseCount}</span></div>
    </div>
  </div>

  <!-- Reminder Cards -->
  <c:choose>
    <c:when test="${empty reminders}">
      <div class="empty-state">
        <i class="fas fa-bell-slash"></i>
        <p>No reminders yet. <a href="${pageContext.request.contextPath}/reminders/add" style="color:var(--accent)">Add your first bill →</a></p>
      </div>
    </c:when>
    <c:otherwise>
      <div class="reminders-grid" id="remindersGrid">
        <c:forEach var="r" items="${reminders}">
        <div class="reminder-card ${!r.active ? 'inactive' : ''}" id="card-${r.id}" data-id="${r.id}" data-title="${r.title}">
          <div class="card-header">
            <div style="display:flex;align-items:center;gap:10px">
              <div class="card-icon" style="background:rgba(99,102,241,.12);color:#818cf8">
                <c:choose>
                  <c:when test="${r.category == 'Utilities'}">      <i class="fas fa-bolt"></i></c:when>
                  <c:when test="${r.category == 'Insurance'}">      <i class="fas fa-shield-halved"></i></c:when>
                  <c:when test="${r.category == 'Health'}">         <i class="fas fa-heart-pulse"></i></c:when>
                  <c:when test="${r.category == 'Entertainment'}">  <i class="fas fa-tv"></i></c:when>
                  <c:when test="${r.category == 'Transport'}">      <i class="fas fa-car"></i></c:when>
                  <c:when test="${r.category == 'Subscriptions'}">  <i class="fas fa-rss"></i></c:when>
                  <c:when test="${r.category == 'Loans'}">          <i class="fas fa-landmark"></i></c:when>
                  <c:when test="${r.category == 'Education'}">      <i class="fas fa-graduation-cap"></i></c:when>
                  <c:otherwise>                                     <i class="fas fa-file-invoice-dollar"></i></c:otherwise>
                </c:choose>
              </div>
              <div>
                <div class="card-title">${r.title}</div>
                <c:if test="${not empty r.note}"><div class="card-note">${r.note}</div></c:if>
              </div>
            </div>
            <div class="card-actions">
              <a href="${pageContext.request.contextPath}/reminders/edit?id=${r.id}"   class="act-btn ab-edit"   title="Edit"><i class="fas fa-pen"></i></a>
              <a href="${pageContext.request.contextPath}/reminders/paid?id=${r.id}"   class="act-btn ab-paid"   title="Mark Paid"
                 onclick="return confirm('Mark \'${r.title}\' as paid and add expense?')"><i class="fas fa-check"></i></a>
              <a href="${pageContext.request.contextPath}/reminders/toggle?id=${r.id}" class="act-btn ab-toggle" title="${r.active ? 'Pause' : 'Activate'}">
                <i class="fas ${r.active ? 'fa-pause' : 'fa-play'}"></i></a>
              <button class="act-btn ab-del" title="Delete" onclick="confirmDelete(${r.id},'${r.title}')"><i class="fas fa-trash"></i></button>
            </div>
          </div>

          <div class="card-amount">₹<fmt:formatNumber value="${r.amount}" pattern="#,##0.00"/></div>

          <div class="card-meta">
            <span class="freq-badge"><i class="fas fa-rotate" style="font-size:.6rem"></i> ${r.frequency}</span>
            <span class="cat-badge">${r.category}</span>
            <c:if test="${r.autoAddExpense}"><span class="auto-badge"><i class="fas fa-bolt" style="font-size:.6rem"></i> Auto</span></c:if>
          </div>

          <div style="display:flex;align-items:center;justify-content:space-between">
            <span class="status-chip ${r.statusClass}">
              <i class="fas ${r.overdue ? 'fa-circle-exclamation' : r.dueToday ? 'fa-bell' : 'fa-calendar-day'}" style="font-size:.6rem"></i>
              ${r.statusLabel}
            </span>
            <span style="font-size:.75rem;color:var(--text-muted)">
              <fmt:formatDate value="${r.nextDueDate}" pattern="dd MMM yyyy"/>
            </span>
          </div>

          <c:if test="${!r.overdue && r.daysUntilDue <= 30}">
          <div class="due-bar-wrap">
            <div class="due-bar-fill" style="width:${100 - (r.daysUntilDue / 30.0 * 100)}%;background:${r.dueToday ? '#f59e0b' : r.dueSoon ? '#fbbf24' : '#10b981'}"></div>
          </div>
          </c:if>
        </div>
        </c:forEach>
      </div>
    </c:otherwise>
  </c:choose>

  <!-- Recent Notification Log -->
  <c:if test="${not empty recentAlerts}">
  <div class="section-card">
    <div class="section-head">
      <h3><i class="fas fa-history" style="color:#f59e0b"></i> Recent Notifications</h3>
      <span style="font-size:.78rem;color:var(--text-muted)">${recentAlerts.size()} records</span>
    </div>
    <div style="padding:4px 20px">
      <c:forEach var="n" items="${recentAlerts}">
      <div class="notif-item">
        <div class="notif-dot ${n.daysBefore < 0 ? 'overdue' : ''}"></div>
        <div class="notif-text">
          <strong style="color:var(--text-primary)">${n.title}</strong>
          &nbsp;·&nbsp;₹<fmt:formatNumber value="${n.amount}" pattern="#,##0.00"/>
          &nbsp;·&nbsp;Due <fmt:formatDate value="${n.nextDueDate}" pattern="dd MMM"/>
          &nbsp;·&nbsp;
          <c:choose>
            <c:when test="${n.daysBefore == 0}">Due today</c:when>
            <c:when test="${n.daysBefore < 0}">Overdue by ${-n.daysBefore}d</c:when>
            <c:otherwise>${n.daysBefore} day(s) before</c:otherwise>
          </c:choose>
        </div>
        <div class="notif-time"><fmt:formatDate value="${n.sentAt}" pattern="dd MMM HH:mm"/></div>
      </div>
      </c:forEach>
    </div>
  </div>
  </c:if>

</div><!-- /page-body -->
</main>

<!-- ── Toast Tray ── -->
<div class="notif-tray" id="notifTray"></div>

<!-- ── Notification Panel (slide-in drawer) ── -->
<div class="notif-panel" id="notifPanel">
  <div class="np-header">
    <h3><i class="fas fa-bell" style="color:#f59e0b;margin-right:8px"></i> Notifications</h3>
    <button class="np-close" onclick="closePanel()"><i class="fas fa-xmark"></i></button>
  </div>
  <div class="np-body" id="npBody">
    <div class="np-empty" id="npEmpty"><i class="fas fa-bell-slash" style="font-size:2rem;opacity:.25;display:block;margin-bottom:12px"></i>No notifications yet.<br>Bills will appear here in real-time.</div>
  </div>
  <div class="np-footer">
    <button class="btn btn-secondary btn-sm" style="width:100%" onclick="clearAll()">
      <i class="fas fa-check-double"></i> Clear All
    </button>
  </div>
</div>
<div id="panelOverlay" onclick="closePanel()" style="display:none;position:fixed;inset:0;z-index:1999;background:rgba(0,0,0,.4)"></div>

<!-- ── Delete Modal ── -->
<div class="modal-overlay" id="deleteModal">
  <div class="modal-box">
    <h3 style="color:#ef4444"><i class="fas fa-trash"></i> Delete Reminder</h3>
    <p>Permanently delete <strong id="delTitle" style="color:var(--text-primary)"></strong>?</p>
    <form method="get" id="deleteForm">
      <div class="modal-actions">
        <button type="button" class="btn btn-secondary" onclick="document.getElementById('deleteModal').classList.remove('open')">Cancel</button>
        <button type="submit" class="btn btn-danger"><i class="fas fa-trash"></i> Delete</button>
      </div>
    </form>
  </div>
</div>

<script>
const CTX = '${pageContext.request.contextPath}';
let unreadCount = 0;
let eventSource = null;
let reconnectTimer = null;
const notifications = []; // in-memory log

/* ═══════════════════════════════════════════
   SSE Connection
═══════════════════════════════════════════ */
function connectSSE() {
  if (eventSource) eventSource.close();

  eventSource = new EventSource(CTX + '/sse/stream');

  eventSource.onopen = () => {
    setSSEStatus(true);
    clearTimeout(reconnectTimer);
    console.log('[SSE] Connected');
  };

  eventSource.onerror = () => {
    setSSEStatus(false);
    eventSource.close();
    // Auto-reconnect after 5 s
    reconnectTimer = setTimeout(connectSSE, 5000);
    console.log('[SSE] Disconnected — retrying in 5s');
  };

  /* ── Event Handlers ── */

  eventSource.addEventListener('connected', e => {
    console.log('[SSE] Server confirmed:', e.data);
  });

  eventSource.addEventListener('bill_due', e => {
    const d = JSON.parse(e.data);
    showToast('bill_due', '🔔 Bill Due Soon',
      d.title + ' — ₹' + fmt(d.amount),
      'Due in ' + d.daysLeft + ' day(s) on ' + d.dueDate,
      d);
    highlightCard(d.title);
  });

  eventSource.addEventListener('due_today', e => {
    const d = JSON.parse(e.data);
    showToast('due_today', '⚡ Due TODAY!',
      d.title + ' — ₹' + fmt(d.amount),
      'Pay now to avoid late fees!',
      d);
    highlightCard(d.title);
    playBeep();
  });

  eventSource.addEventListener('overdue', e => {
    const d = JSON.parse(e.data);
    showToast('overdue', '🚨 OVERDUE!',
      d.title + ' — ₹' + fmt(d.amount),
      'Overdue by ' + Math.abs(d.daysLeft) + ' day(s)! Pay immediately.',
      d);
    highlightCard(d.title);
    playBeep();
  });

  eventSource.addEventListener('auto_expense', e => {
    const d = JSON.parse(e.data);
    showToast('auto_expense', '✅ Auto-Expense Added',
      d.title + ' — ₹' + fmt(d.amount),
      'Expense automatically recorded.',
      d);
  });

  eventSource.addEventListener('paid', e => {
    const d = JSON.parse(e.data);
    showToast('paid', '💚 Bill Paid',
      d.title + ' — ₹' + fmt(d.amount),
      d.message,
      d);
  });
}

/* ═══════════════════════════════════════════
   Toast UI
═══════════════════════════════════════════ */
function showToast(type, heading, title, msg, data) {
  // Store in panel
  notifications.unshift({ type, heading, title, msg, time: new Date(), data });
  refreshPanel();

  // Show toast
  const tray  = document.getElementById('notifTray');
  const toast = document.createElement('div');
  toast.className = 'toast';
  toast.dataset.type = type;
  toast.innerHTML =
    '<div class="toast-accent ' + type + '"></div>' +
    '<div class="toast-body">' +
      '<div class="toast-header">' +
        '<span class="toast-title">' + escHtml(heading) + '</span>' +
        '<button class="toast-close" onclick="removeToast(this)"><i class="fas fa-xmark"></i></button>' +
      '</div>' +
      '<div class="toast-msg"><strong>' + escHtml(title) + '</strong><br>' + escHtml(msg) + '</div>' +
      '<div class="toast-time">' + new Date().toLocaleTimeString() + '</div>' +
      '<div class="toast-actions">' +
        '<button class="ta-btn ta-view" onclick="window.location.href=\'${CTX}/reminders/list\'">View Bills</button>' +
        '<button class="ta-btn ta-dismiss" onclick="removeToast(this)">Dismiss</button>' +
      '</div>' +
    '</div>';
  tray.appendChild(toast);

  // Update bell
  unreadCount++;
  updateBell();

  // Native browser notification
  if (Notification.permission === 'granted') {
    new Notification(heading, { body: title + '\n' + msg });
  }

  // Auto-dismiss non-critical toasts after 8s
  if (type !== 'overdue' && type !== 'due_today') {
    setTimeout(() => { if (toast.parentNode) removeToast(toast.querySelector('.toast-close')); }, 8000);
  }
}

function removeToast(btn) {
  const toast = btn.closest('.toast');
  toast.style.animation = 'toastOut .3s ease forwards';
  setTimeout(() => toast.remove(), 300);
}

/* ═══════════════════════════════════════════
   Notification Panel
═══════════════════════════════════════════ */
function openPanel() {
  document.getElementById('notifPanel').classList.add('open');
  document.getElementById('panelOverlay').style.display = 'block';
  unreadCount = 0; updateBell();
}
function closePanel() {
  document.getElementById('notifPanel').classList.remove('open');
  document.getElementById('panelOverlay').style.display = 'none';
}
function clearAll() {
  notifications.length = 0;
  document.querySelectorAll('.toast').forEach(t => t.remove());
  unreadCount = 0; updateBell();
  refreshPanel();
}
function refreshPanel() {
  const body  = document.getElementById('npBody');
  const empty = document.getElementById('npEmpty');
  if (notifications.length === 0) { if(empty) empty.style.display='block'; return; }
  if (empty) empty.style.display = 'none';
  // Re-render all items
  body.innerHTML = notifications.map(function(n) {
    return '<div class="np-item">' +
      '<div class="np-item-title">' + escHtml(n.heading) + ' &mdash; ' + escHtml(n.title) + '</div>' +
      '<div class="np-item-sub">' + escHtml(n.msg) + '<br><span style="opacity:.6">' + n.time.toLocaleTimeString() + '</span></div>' +
    '</div>';
  }).join('');
}

/* ═══════════════════════════════════════════
   Helpers
═══════════════════════════════════════════ */
function updateBell() {
  const badge = document.getElementById('bellBadge');
  const icon  = document.getElementById('bellIcon');
  if (unreadCount > 0) {
    badge.style.display = 'flex';
    badge.textContent   = unreadCount > 9 ? '9+' : unreadCount;
    icon.classList.add('bell-shake');
    setTimeout(() => icon.classList.remove('bell-shake'), 600);
  } else {
    badge.style.display = 'none';
  }
}

function setSSEStatus(online) {
  const el    = document.getElementById('sseStatus');
  const label = document.getElementById('sseLabel');
  el.className = 'sse-status ' + (online ? 'sse-live' : 'sse-offline');
  label.textContent = online ? 'Live' : 'Offline';
}

function highlightCard(title) {
  document.querySelectorAll('.reminder-card').forEach(card => {
    if (card.dataset.title === title) {
      card.classList.add('highlight');
      setTimeout(() => card.classList.remove('highlight'), 2000);
    }
  });
}

function confirmDelete(id, title) {
  document.getElementById('delTitle').textContent = title;
  document.getElementById('deleteForm').action = CTX + '/reminders/delete?id=' + id;
  document.getElementById('deleteModal').classList.add('open');
}
document.getElementById('deleteModal').addEventListener('click', e => {
  if (e.target === document.getElementById('deleteModal'))
    document.getElementById('deleteModal').classList.remove('open');
});

function fmt(amt) { return parseFloat(amt).toLocaleString('en-IN', {minimumFractionDigits:2}); }
function escHtml(s) { const d=document.createElement('div'); d.textContent=s||''; return d.innerHTML; }

function playBeep() {
  try {
    const ctx = new (window.AudioContext || window.webkitAudioContext)();
    const osc = ctx.createOscillator();
    const gain = ctx.createGain();
    osc.connect(gain); gain.connect(ctx.destination);
    osc.frequency.value = 880;
    osc.type = 'sine';
    gain.gain.setValueAtTime(0.3, ctx.currentTime);
    gain.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + 0.4);
    osc.start(ctx.currentTime);
    osc.stop(ctx.currentTime + 0.4);
  } catch(e) {}
}

/* ── Init ── */
// Request native notification permission
if ('Notification' in window && Notification.permission === 'default') {
  Notification.requestPermission();
}

// Start SSE
connectSSE();

// Auto-dismiss flash alerts
setTimeout(() => {
  document.querySelectorAll('.alert').forEach(a => {
    a.style.transition = 'opacity .5s'; a.style.opacity = '0';
    setTimeout(() => a.remove(), 500);
  });
}, 3500);
</script>
</body>
</html>
