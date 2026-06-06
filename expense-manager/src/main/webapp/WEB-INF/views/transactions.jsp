<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle"  value="Transactions" scope="request"/>
<c:set var="activePage" value="txn"          scope="request"/>
<c:set var="currentYear" value="<%=java.time.Year.now().getValue()%>" scope="request"/>
<%@ include file="header.jsp" %>

<div class="page-header flex">
  <div>
    <h1>Transactions</h1>
    <p>All income &amp; expense records (${total} total)</p>
  </div>
  <div class="flex gap-1 ml-auto">
    <button class="btn btn-success btn-sm" onclick="openModal('incomeModal')">+ Income</button>
    <button class="btn btn-danger btn-sm"  onclick="openModal('expenseModal')">+ Expense</button>
    <button class="btn btn-outline btn-sm" onclick="openModal('catModal')">+ Category</button>
    <button class="btn btn-outline btn-sm" onclick="openModal('colModal')">+ Column</button>
  </div>
</div>

<c:if test="${not empty param.success}">
  <div class="alert alert-success">✓
    <c:choose>
      <c:when test="${param.success=='1'}">Transaction saved successfully!</c:when>
      <c:when test="${param.success=='cat'}">Category added!</c:when>
      <c:when test="${param.success=='col'}">Custom column added!</c:when>
    </c:choose>
  </div>
</c:if>
<c:if test="${not empty param.error}">
  <div class="alert alert-error">✗ Error: ${param.error}</div>
</c:if>
<c:if test="${not empty dbError}">
  <div class="alert alert-error">✗ DB: ${dbError}</div>
</c:if>

<!-- Filter tabs -->
<div class="tabs">
  <a href="${pageContext.request.contextPath}/transactions"
     class="tab ${empty param.filter?'active':''}">All</a>
  <a href="${pageContext.request.contextPath}/transactions?filter=INCOME"
     class="tab income ${param.filter=='INCOME'?'active':''}">Income</a>
  <a href="${pageContext.request.contextPath}/transactions?filter=EXPENSE"
     class="tab expense ${param.filter=='EXPENSE'?'active':''}">Expenses</a>
</div>

<div class="table-wrap">
  <table>
    <thead>
      <tr>
        <th>#</th>
        <th>Date &amp; Time</th>
        <th>Type</th>
        <th>Category</th>
        <th>Amount</th>
        <th>Note</th>
        <c:if test="${not empty param.filter}">
          <c:forEach var="col" items="${param.filter=='INCOME'?incomeColumns:expenseColumns}">
            <th>${col.colName}</th>
          </c:forEach>
        </c:if>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="t" items="${transactions}" varStatus="st">
        <tr>
          <td class="text-muted" style="font-size:.8rem">${total - ((page-1)*15) - st.index}</td>
          <td class="text-muted" style="font-size:.82rem;white-space:nowrap">
            <fmt:formatDate value="${t.dateTime}" pattern="dd MMM yy HH:mm"/>
          </td> 
          
          <!-- <td class="text-muted" style="font-size:.82rem;white-space:nowrap"> ${t.formattedDateTime} </td>  --> 
          
          <td><span class="badge ${t.type=='INCOME'?'income':'expense'}">${t.type}</span></td>
          <td><span class="chip">${t.categoryName}</span></td>
          <td class="${t.type=='INCOME'?'amount-pos':'amount-neg'}">
            ${t.type=='INCOME'?'+':'-'}₹<fmt:formatNumber value="${t.amount}" pattern="#,##0.00"/>
          </td>
          <td class="text-muted" style="max-width:180px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">${t.note}</td>
          <c:if test="${not empty param.filter}">
            <c:forEach var="col" items="${param.filter=='INCOME'?incomeColumns:expenseColumns}">
              <td class="text-muted">${t.customValues[col.colKey]}</td>
            </c:forEach>
          </c:if>
        </tr>
      </c:forEach>
      <c:if test="${empty transactions}">
        <tr><td colspan="8" class="empty-state">No transactions found.</td></tr>
      </c:if>
    </tbody>
  </table>
</div>

<!-- Pagination -->
<c:if test="${totalPages > 1}">
  <div class="pagination mt-2">
    <c:forEach begin="1" end="${totalPages}" var="p">
      <a href="${pageContext.request.contextPath}/transactions?page=${p}<c:if test="${not empty param.filter}">&filter=${param.filter}</c:if>"
         class="page-btn ${p==page?'active':''}">${p}</a>
    </c:forEach>
  </div>
</c:if>

<%@ include file="txn_modals.jsp" %>
<%@ include file="footer.jsp" %>
