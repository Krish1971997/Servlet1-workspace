<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- These modals need incomeCategories, expenseCategories, incomeColumns, expenseColumns in scope.
     If not set (dashboard), load them here --%>
<%
  if (request.getAttribute("incomeCategories") == null) {
    try {
      com.expensemanager.dao.CategoryDAO cDao = new com.expensemanager.dao.CategoryDAO();
      com.expensemanager.dao.ColumnDefinitionDAO colDao = new com.expensemanager.dao.ColumnDefinitionDAO();
      request.setAttribute("incomeCategories",  cDao.findByType("INCOME"));
      request.setAttribute("expenseCategories", cDao.findByType("EXPENSE"));
      request.setAttribute("incomeColumns",  colDao.findByType("INCOME"));
      request.setAttribute("expenseColumns", colDao.findByType("EXPENSE"));
    } catch (Exception e) { /* DB not ready */ }
  }
%>

<!-- ── Add Income Modal ── -->
<div id="incomeModal" class="modal-overlay">
  <div class="modal">
    <div class="modal-header">
      <h3 style="color:var(--green)">＋ Add Income</h3>
      <button class="modal-close" onclick="closeModal('incomeModal')">✕</button>
    </div>
    <form id="incomeForm" action="${pageContext.request.contextPath}/transactions" method="post">
      <input type="hidden" name="type" value="INCOME">
      <div class="form-grid">
        <div class="form-group">
          <label>Date &amp; Time *</label>
          <input type="datetime-local" name="dateTime" required>
        </div>
        <div class="form-group">
          <label>Amount (₹) *</label>
          <input type="number" name="amount" min="0.01" step="0.01" placeholder="0.00" required>
        </div>
        <div class="form-group">
          <label>Category *</label>
          <select name="categoryId" required>
            <option value="">Select…</option>
            <c:forEach var="cat" items="${incomeCategories}">
              <option value="${cat.id}">${cat.name}</option>
            </c:forEach>
          </select>
        </div>
        <div class="form-group" style="grid-column:1/-1">
          <label>Note</label>
          <input type="text" name="note" placeholder="Optional">
        </div>
      </div>
      <!-- Existing custom columns -->
      <c:if test="${not empty incomeColumns}">
        <div style="margin-top:.75rem;border-top:1px solid var(--border);padding-top:.75rem;">
          <div class="card-title">Custom Fields</div>
          <div class="form-grid">
            <c:forEach var="col" items="${incomeColumns}">
              <div class="form-group">
                <label>${col.colName}</label>
                <input type="text" name="custom_${col.colKey}" placeholder="${col.colName}">
              </div>
            </c:forEach>
          </div>
        </div>
      </c:if>
      <!-- Dynamic extra fields -->
      <div id="incExtras"></div>
      <div class="flex gap-1 mt-2">
        <button type="button" class="btn btn-outline btn-sm" onclick="addCustomField('incExtras')">+ Ad-hoc Field</button>
        <button type="button" class="btn btn-success ml-auto" onclick="submitForm('incomeForm')">Save Income</button>
      </div>
    </form>
  </div>
</div>

<!-- ── Add Expense Modal ── -->
<div id="expenseModal" class="modal-overlay">
  <div class="modal">
    <div class="modal-header">
      <h3 style="color:var(--red)">＋ Add Expense</h3>
      <button class="modal-close" onclick="closeModal('expenseModal')">✕</button>
    </div>
    <form id="expenseForm" action="${pageContext.request.contextPath}/transactions" method="post">
      <input type="hidden" name="type" value="EXPENSE">
      <div class="form-grid">
        <div class="form-group">
          <label>Date &amp; Time *</label>
          <input type="datetime-local" name="dateTime" required>
        </div>
        <div class="form-group">
          <label>Amount (₹) *</label>
          <input type="number" name="amount" min="0.01" step="0.01" placeholder="0.00" required>
        </div>
        <div class="form-group">
          <label>Category *</label>
          <select name="categoryId" required>
            <option value="">Select…</option>
            <c:forEach var="cat" items="${expenseCategories}">
              <option value="${cat.id}">${cat.name}</option>
            </c:forEach>
          </select>
        </div>
        <div class="form-group" style="grid-column:1/-1">
          <label>Note</label>
          <input type="text" name="note" placeholder="Optional">
        </div>
      </div>
      <c:if test="${not empty expenseColumns}">
        <div style="margin-top:.75rem;border-top:1px solid var(--border);padding-top:.75rem;">
          <div class="card-title">Custom Fields</div>
          <div class="form-grid">
            <c:forEach var="col" items="${expenseColumns}">
              <div class="form-group">
                <label>${col.colName}</label>
                <input type="text" name="custom_${col.colKey}" placeholder="${col.colName}">
              </div>
            </c:forEach>
          </div>
        </div>
      </c:if>
      <div id="expExtras"></div>
      <div class="flex gap-1 mt-2">
        <button type="button" class="btn btn-outline btn-sm" onclick="addCustomField('expExtras')">+ Ad-hoc Field</button>
        <button type="button" class="btn btn-danger ml-auto" onclick="submitForm('expenseForm')">Save Expense</button>
      </div>
    </form>
  </div>
</div>

<!-- ── Add Category Modal ── -->
<div id="catModal" class="modal-overlay">
  <div class="modal">
    <div class="modal-header">
      <h3>Add Category</h3>
      <button class="modal-close" onclick="closeModal('catModal')">✕</button>
    </div>
    <form action="${pageContext.request.contextPath}/categories" method="post">
      <input type="hidden" name="back" value="/transactions">
      <div class="form-grid">
        <div class="form-group">
          <label>Category Name *</label>
          <input type="text" name="name" placeholder="e.g. Rent" required>
        </div>
        <div class="form-group">
          <label>Type *</label>
          <select name="type" required>
            <option value="INCOME">Income</option>
            <option value="EXPENSE">Expense</option>
          </select>
        </div>
      </div>
      <div class="flex mt-2">
        <button type="submit" class="btn btn-primary ml-auto">Save Category</button>
      </div>
    </form>
  </div>
</div>

<!-- ── Add Custom Column Modal ── -->
<div id="colModal" class="modal-overlay">
  <div class="modal">
    <div class="modal-header">
      <h3>Add Custom Column</h3>
      <button class="modal-close" onclick="closeModal('colModal')">✕</button>
    </div>
    <form action="${pageContext.request.contextPath}/columns" method="post">
      <div class="form-grid">
        <div class="form-group">
          <label>Column Name *</label>
          <input type="text" name="colName" placeholder="e.g. Invoice No" required>
        </div>
        <div class="form-group">
          <label>For Type *</label>
          <select name="type" required>
            <option value="INCOME">Income</option>
            <option value="EXPENSE">Expense</option>
          </select>
        </div>
      </div>
      <p class="text-muted mt-1" style="font-size:.78rem">This column will appear in all future transactions of this type.</p>
      <div class="flex mt-2">
        <button type="submit" class="btn btn-primary ml-auto">Save Column</button>
      </div>
    </form>
  </div>
</div>
