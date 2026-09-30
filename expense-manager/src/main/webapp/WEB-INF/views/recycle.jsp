<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="pageTitle" value="Recycle Bin" scope="request" />
<c:set var="activePage" value="recycle" scope="request" />
<%@ include file="header.jsp"%>

<div class="page-header" style="margin-bottom: 1rem">
	<h2>&#x1F5D1; Recycle Bin</h2>
	<p style="font-size: .85rem; color: var(--text-2)">
		Soft-deleted records for the active book. Restore puts the record back
		with its original id and child rows (receipts, custom fields, audit
		log); purge removes it permanently.</p>
</div>

<c:if test="${not empty param.msg}">
	<div class="alert alert-success">&#10003; ${param.msg == 'restored' ? 'Record restored.' : 'Record purged permanently.'}</div>
</c:if>
<c:if test="${not empty dbError}">
	<div class="alert alert-error">&#10007; ${dbError}</div>
</c:if>

<div class="card" style="padding: 0">
	<div class="table-wrap">
		<table>
			<thead>
				<tr>
					<th>Deleted At</th>
					<th>Type</th>
					<th>Record</th>
					<th style="width: 180px"></th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${empty items}">
					<tr>
						<td colspan="4" style="text-align: center; color: var(--text-2)">Recycle
							bin is empty.</td>
					</tr>
				</c:if>
				<c:forEach var="it" items="${items}">
					<tr>
						<td>${it.deletedAt}</td>
						<td><span class="badge">${it.tableName}</span></td>
						<td>${it.label}</td>
						<td>
							<form method="post" action="${pageContext.request.contextPath}/recycle"
								style="display: inline">
								<input type="hidden" name="action" value="restore"> <input
									type="hidden" name="id" value="${it.id}">
								<button type="submit" class="btn btn-success btn-sm">&#10003;
									Restore</button>
							</form>
							<form method="post" action="${pageContext.request.contextPath}/recycle"
								style="display: inline"
								onsubmit="return confirm('Delete permanently? This cannot be undone.')">
								<input type="hidden" name="action" value="purge"> <input
									type="hidden" name="id" value="${it.id}">
								<button type="submit" class="btn btn-danger btn-sm">&#x1F5D1;
									Purge</button>
							</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<%@ include file="footer.jsp"%>
