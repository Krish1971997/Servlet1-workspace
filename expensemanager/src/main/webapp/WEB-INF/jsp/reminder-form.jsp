<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1.0">
<title>ExpenseIQ – ${not empty reminder ? 'Edit' : 'Add'} Reminder</title>
<link href="https://fonts.googleapis.com/css2?family=Syne:wght@400;600;700;800&family=DM+Sans:wght@300;400;500&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<style>
.freq-cards{display:grid;grid-template-columns:repeat(3,1fr);gap:10px;margin-bottom:6px}
.freq-card{border:1.5px solid var(--border);border-radius:12px;padding:12px 14px;cursor:pointer;transition:.15s;text-align:center}
.freq-card:hover{border-color:var(--accent)}
.freq-card.selected{border-color:var(--accent);background:rgba(99,102,241,.08)}
.freq-card i{font-size:1.2rem;color:var(--text-muted);display:block;margin-bottom:5px}
.freq-card span{font-size:.8rem;font-weight:600;color:var(--text-secondary)}
.freq-card.selected i,.freq-card.selected span{color:#818cf8}
.preview-box{background:rgba(99,102,241,.06);border:1px solid rgba(99,102,241,.2);border-radius:12px;padding:14px 18px;margin-top:16px;font-size:.85rem;color:var(--text-secondary);line-height:1.8}
.preview-box strong{color:var(--text-primary)}
.toggle-row{display:flex;align-items:center;justify-content:space-between;padding:12px 0;border-top:1px solid var(--border)}
.toggle-row label{font-size:.88rem;color:var(--text-secondary)}
.toggle-switch{position:relative;width:42px;height:24px}
.toggle-switch input{opacity:0;width:0;height:0}
.toggle-slider{position:absolute;inset:0;background:var(--border);border-radius:12px;cursor:pointer;transition:.2s}
.toggle-slider::before{content:'';position:absolute;width:18px;height:18px;left:3px;top:3px;background:#fff;border-radius:50%;transition:.2s}
input:checked + .toggle-slider{background:var(--accent)}
input:checked + .toggle-slider::before{transform:translateX(18px)}
</style>
</head>
<body>

<nav class="sidebar">
<div class="sidebar-brand"><span class="brand-icon">💰</span><span class="brand-name">ExpenseIQ</span></div>
<ul class="nav-links">
  <li><a href="${pageContext.request.contextPath}/dashboard"><i class="fas fa-chart-pie"></i><span>Dashboard</span></a></li>
  <li><a href="${pageContext.request.contextPath}/income/list"><i class="fas fa-arrow-trend-up"></i><span>Income</span></a></li>
  <li><a href="${pageContext.request.contextPath}/expense/list"><i class="fas fa-arrow-trend-down"></i><span>Expenses</span></a></li>
  <li><a href="${pageContext.request.contextPath}/reports"><i class="fas fa-chart-bar"></i><span>Reports</span></a></li>
  <li><a href="${pageContext.request.contextPath}/reminders/list" class="active"><i class="fas fa-bell"></i><span>Reminders</span></a></li>
  <li><a href="${pageContext.request.contextPath}/backup/list"><i class="fas fa-database"></i><span>Backup</span></a></li>
</ul>
<div class="sidebar-footer"><span>v1.0.0</span></div>
</nav>

<main class="main-content">
<div class="topbar">
  <div class="page-title">${not empty reminder ? 'Edit' : 'Add'} Bill Reminder</div>
  <a href="${pageContext.request.contextPath}/reminders/list" class="btn btn-secondary btn-sm"><i class="fas fa-arrow-left"></i> Back</a>
</div>
<div class="page-body">
<div class="form-card" style="max-width:700px">

  <div style="display:flex;align-items:center;gap:14px;padding-bottom:20px;border-bottom:1px solid var(--border);margin-bottom:24px">
    <div style="width:46px;height:46px;border-radius:13px;background:rgba(245,158,11,.15);color:#f59e0b;display:flex;align-items:center;justify-content:center;font-size:1.3rem">
      <i class="fas fa-bell"></i>
    </div>
    <div>
      <h2 style="font-family:'Syne',sans-serif;font-size:1.2rem;font-weight:800;margin:0">
        ${not empty reminder ? 'Edit' : 'New'} Bill Reminder
      </h2>
      <p style="color:var(--text-muted);font-size:.82rem;margin:2px 0 0">
        ${not empty reminder ? 'Update reminder details' : 'Set up a recurring bill to track'}
      </p>
    </div>
  </div>

  <form method="post" action="${pageContext.request.contextPath}/reminders/save" id="reminderForm">
    <c:if test="${not empty reminder}">
      <input type="hidden" name="id" value="${reminder.id}">
    </c:if>

    <div class="form-grid">
      <div class="form-group">
        <label>Bill Title <span style="color:#f87171">*</span></label>
        <input type="text" name="title" class="form-control" required
               placeholder="e.g. Electricity Bill, Netflix…"
               value="${not empty reminder ? reminder.title : ''}">
      </div>
      <div class="form-group">
        <label>Amount (₹) <span style="color:#f87171">*</span></label>
        <input type="number" name="amount" class="form-control" required
               step="0.01" min="1" placeholder="0.00" id="amountInput"
               value="${not empty reminder ? reminder.amount : ''}">
      </div>
      <div class="form-group">
        <label>Category</label>
        <select name="category" class="form-control" id="catSelect">
          <c:set var="cats" value="Utilities,Insurance,Health,Entertainment,Transport,Education,Rent,Subscriptions,Loans,Bills,Other"/>
          <c:forTokens items="${cats}" delims="," var="cat">
            <option value="${cat}" ${not empty reminder && reminder.category == cat ? 'selected' : ''}>${cat}</option>
          </c:forTokens>
        </select>
      </div>
      <div class="form-group">
        <label>Next Due Date <span style="color:#f87171">*</span></label>
        <input type="date" name="nextDueDate" class="form-control" required id="dueDateInput"
               value="${not empty reminder ? reminder.nextDueDate : ''}">
      </div>
    </div>

    <!-- Frequency picker -->
    <div class="form-group">
      <label>Frequency</label>
      <div class="freq-cards">
        <c:forEach var="freq" items="${frequencies}">
        <div class="freq-card ${not empty reminder && reminder.frequency.name() == freq.name() ? 'selected' : (empty reminder && freq.name() == 'MONTHLY') ? 'selected' : ''}"
             onclick="selectFreq('${freq.name()}', this)">
          <i class="fas ${freq.name() == 'DAILY' ? 'fa-sun' : freq.name() == 'WEEKLY' ? 'fa-calendar-week' : freq.name() == 'MONTHLY' ? 'fa-calendar' : freq.name() == 'QUARTERLY' ? 'fa-chart-line' : freq.name() == 'YEARLY' ? 'fa-calendar-check' : 'fa-circle-dot'}"></i>
          <span>${freq.name()}</span>
        </div>
        </c:forEach>
      </div>
      <input type="hidden" name="frequency" id="freqInput"
             value="${not empty reminder ? reminder.frequency.name() : 'MONTHLY'}">
    </div>

    <div class="form-grid">
      <div class="form-group">
        <label>Remind Me (days before)</label>
        <select name="remindDaysBefore" class="form-control" id="remindSelect">
          <c:forEach begin="1" end="14" var="d">
            <option value="${d}" ${not empty reminder && reminder.remindDaysBefore == d ? 'selected' : d == 3 ? 'selected' : ''}>${d} day${d == 1 ? '' : 's'} before</option>
          </c:forEach>
        </select>
      </div>
      <div class="form-group">
        <label>Note (optional)</label>
        <input type="text" name="note" class="form-control"
               placeholder="Provider, account number…"
               value="${not empty reminder ? reminder.note : ''}">
      </div>
    </div>

    <!-- Auto-add toggle -->
    <div class="toggle-row">
      <div>
        <label style="font-weight:600;font-size:.88rem">Auto-add expense when due</label>
        <div style="font-size:.78rem;color:var(--text-muted);margin-top:2px">Automatically create an expense entry on due date</div>
      </div>
      <label class="toggle-switch">
        <input type="checkbox" name="autoAddExpense" id="autoToggle"
               ${not empty reminder && reminder.autoAddExpense ? 'checked' : ''}>
        <span class="toggle-slider"></span>
      </label>
    </div>

    <!-- Live preview -->
    <div class="preview-box" id="previewBox">
      <i class="fas fa-eye" style="color:#818cf8;margin-right:6px"></i>
      <strong id="prevTitle">Your Bill</strong> of
      <strong id="prevAmt">₹0</strong> —
      <strong id="prevFreq">Monthly</strong>, next due
      <strong id="prevDate">—</strong>.
      Reminder <strong id="prevDays">3</strong> days before.
      <span id="prevAuto"></span>
    </div>

    <div style="display:flex;gap:12px;margin-top:20px">
      <button type="submit" class="btn btn-primary">
        <i class="fas fa-bell"></i> ${not empty reminder ? 'Update' : 'Create'} Reminder
      </button>
      <a href="${pageContext.request.contextPath}/reminders/list" class="btn btn-secondary">
        <i class="fas fa-xmark"></i> Cancel
      </a>
    </div>
  </form>
</div>
</div>
</main>

<script>
function selectFreq(val, el) {
  document.querySelectorAll('.freq-card').forEach(c => c.classList.remove('selected'));
  el.classList.add('selected');
  document.getElementById('freqInput').value = val;
  updatePreview();
}

function updatePreview() {
  const title  = document.querySelector('[name=title]').value     || 'Your Bill';
  const amt    = document.getElementById('amountInput').value     || '0';
  const freq   = document.getElementById('freqInput').value       || 'MONTHLY';
  const date   = document.getElementById('dueDateInput').value    || '—';
  const days   = document.getElementById('remindSelect').value    || '3';
  const auto   = document.getElementById('autoToggle').checked;
  document.getElementById('prevTitle').textContent = title;
  document.getElementById('prevAmt').textContent   = '₹' + parseFloat(amt||0).toLocaleString('en-IN',{minimumFractionDigits:2});
  document.getElementById('prevFreq').textContent  = freq.charAt(0) + freq.slice(1).toLowerCase();
  document.getElementById('prevDate').textContent  = date;
  document.getElementById('prevDays').textContent  = days;
  document.getElementById('prevAuto').textContent  = auto ? '⚡ Auto-expense enabled.' : '';
}

document.querySelectorAll('[name=title],[name=amount]').forEach(el => el.addEventListener('input', updatePreview));
document.getElementById('dueDateInput').addEventListener('change', updatePreview);
document.getElementById('remindSelect').addEventListener('change', updatePreview);
document.getElementById('autoToggle').addEventListener('change', updatePreview);

// Set default date to today if adding new
window.addEventListener('DOMContentLoaded', () => {
  const d = document.getElementById('dueDateInput');
  if (!d.value) d.value = new Date().toISOString().split('T')[0];
  updatePreview();
});
</script>
</body>
</html>
