<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${pageTitle} — ExpenseOS</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>

<nav class="navbar">
  <a class="nav-brand" href="${pageContext.request.contextPath}/home">
    <span>&#128200;</span> ExpenseOS
  </a>

  <%-- Active book badge --%>
  <c:if test="${not empty sessionScope.activeBookName}">
    <a href="${pageContext.request.contextPath}/books"
       style="display:flex;align-items:center;gap:.35rem;background:#eff6ff;border:1px solid #bfdbfe;
              color:var(--primary);padding:.3rem .8rem;border-radius:20px;font-size:.8rem;
              font-weight:600;text-decoration:none;margin-left:.5rem;white-space:nowrap"
       title="Switch book">
      &#128216; ${sessionScope.activeBookName}
      <span style="font-size:.65rem;opacity:.7">&#9660;</span>
    </a>
  </c:if>

  <div class="nav-links">
    <a href="${pageContext.request.contextPath}/home"
       class="${activePage=='home'?'active':''}">Dashboard</a>
    <a href="${pageContext.request.contextPath}/transactions"
       class="${activePage=='txn'?'active':''}">Transactions</a>
    <a href="${pageContext.request.contextPath}/reports"
       class="${activePage=='reports'?'active':''}">Reports</a>
    <a href="${pageContext.request.contextPath}/books"
       class="${activePage=='books'?'active':''}">Books</a>
  </div>

  <%-- Settings always visible --%>
  <a href="${pageContext.request.contextPath}/settings"
     class="btn btn-outline btn-sm ${activePage=='settings'?'':''}">
    &#9881; Settings
  </a>
</nav>

<div class="page">
