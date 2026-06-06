<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle"  value="Dashboard" scope="request"/>
<c:set var="activePage" value="home"      scope="request"/>
<c:set var="currentYear" value="<%=java.time.Year.now().getValue()%>" scope="request"/>
<%@ include file="header.jsp" %>

<div class="page-header flex">
  <div>
    <h1>Dashboard</h1>
    <p>Your financial overview at a glance</p>
  </div>
  <div class="flex gap-1 ml-auto">
    <button class="btn btn-success" onclick="openModal('incomeModal')">+ Income</button>
    <button class="btn btn-danger"  onclick="openModal('expenseModal')">+ Expense</button>
  </div>
</div>

<c:if test="${not empty dbError}">
  <div class="alert alert-error">⚠ DB Error: ${dbError}</div>
</c:if>

<!-- Stats -->
<div class="stats-grid">
  <div class="stat-card">
    <div class="stat-label">Total Income</div>
    <div class="stat-value income">₹<fmt:formatNumber value="${totalIncome}" pattern="#,##0.00"/></div>
  </div>
  <div class="stat-card">
    <div class="stat-label">Total Expenses</div>
    <div class="stat-value expense">₹<fmt:formatNumber value="${totalExpense}" pattern="#,##0.00"/></div>
  </div>
  <div class="stat-card">
    <div class="stat-label">Net Balance</div>
    <div class="stat-value balance">₹<fmt:formatNumber value="${balance}" pattern="#,##0.00"/></div>
  </div>
</div>

<!-- Recent Transactions -->
<div class="card">
  <div class="flex mb-2">
    <span class="card-title" style="margin-bottom:0">Recent Transactions</span>
    <a href="${pageContext.request.contextPath}/transactions" class="btn btn-outline btn-sm ml-auto">View All →</a>
  </div>
  <div class="table-wrap">
    <table>
      <thead>
        <tr><th>Date</th><th>Type</th><th>Category</th><th>Amount</th><th>Note</th></tr>
      </thead>
      <tbody>
        <c:forEach var="t" items="${recentTxns}">
          <tr>
            <!-- <td class="text-muted"> -->
              <fmt:formatDate value="${t.dateTime}" pattern="dd MMM yyyy"/>
              <!-- <fmt:formatDate value="${t.dateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy"))}" pattern="dd MMM yyyy"/> 
              <td class="text-muted">${t.formattedDate}</td> -->
              
           </td> 
            <td><span class="badge ${t.type=='INCOME'?'income':'expense'}">${t.type}</span></td>
            <td><span class="chip">${t.categoryName}</span></td>
            <td class="${t.type=='INCOME'?'amount-pos':'amount-neg'}">
              ${t.type=='INCOME'?'+':'-'}₹<fmt:formatNumber value="${t.amount}" pattern="#,##0.00"/>
            </td>
            <td class="text-muted">${t.note}</td>
          </tr>
        </c:forEach>
        <c:if test="${empty recentTxns}">
          <tr><td colspan="5" class="empty-state">No transactions yet. Add one above!</td></tr>
        </c:if>
      </tbody>
    </table>
  </div>
</div>

<!-- Quick links -->
<div class="flex gap-2 mt-3">
  <a href="${pageContext.request.contextPath}/transactions?filter=INCOME"  class="btn btn-outline">View Income →</a>
  <a href="${pageContext.request.contextPath}/transactions?filter=EXPENSE" class="btn btn-outline">View Expenses →</a>
  <a href="${pageContext.request.contextPath}/reports" class="btn btn-primary ml-auto">Analytics →</a>
</div>

<%@ include file="txn_modals.jsp" %>
<%@ include file="footer.jsp" %>
