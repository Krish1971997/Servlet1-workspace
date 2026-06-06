<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${pageTitle} — Expense Manager</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<nav class="navbar">
  <a class="nav-brand" href="${pageContext.request.contextPath}/home">
    <span>💰</span> ExpenseOS
  </a>
  <div class="nav-links">
    <a href="${pageContext.request.contextPath}/home"
       class="${activePage=='home'?'active':''}">Dashboard</a>
    <a href="${pageContext.request.contextPath}/transactions"
       class="${activePage=='txn'?'active':''}">Transactions</a>
    <a href="${pageContext.request.contextPath}/reports"
       class="${activePage=='reports'?'active':''}">Reports</a>
  </div>
</nav>
<div class="page">
