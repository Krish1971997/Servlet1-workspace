<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="pageTitle" value="Settlement Links" scope="request" />
<c:set var="activePage" value="txn" scope="request" />
<%@ include file="header.jsp"%>

<c:if test="${not empty txn}">
	<div class="page-header" style="margin-bottom: 1rem">
		<h2>&#128278; Settlement Links &mdash; Transaction #${txn.id}</h2>
		<p style="font-size: .85rem; color: var(--text-2)">
			${txn.formattedDateTime} &middot; ${txn.type} &middot;
			&#8377;${txn.amount}
			<c:if test="${not empty txn.note}">&middot; ${txn.note}</c:if></p>
	</div>

	<c:if test="${not empty param.msg}">
		<div class="alert alert-success">&#10003; Saved.</div>
	</c:if>
	<c:if test="${not empty param.error}">
		<div class="alert alert-error">&#10007; Operation failed.</div>
	</c:if>

	<div class="card mb-2">
		<div class="card-title">Linked settlements &mdash; total linked
			away: <strong>&#8377;<fmt:formatNumber value="${linkedTotal}"
					pattern="#,##0.00" /></strong></div>
		<c:if test="${empty linkRows}">
			<p style="font-size: .85rem; color: var(--text-2)">No links yet.</p>
		</c:if>
		<c:forEach var="l" items="${linkRows}">
			<div class="flex gap-1" style="align-items: center; padding: .4rem 0; border-bottom: 1px solid var(--border)">
				<span>&#8377;<fmt:formatNumber value="${l.amount}" pattern="#,##0.00" />
					&middot; linked to ${l.otherLabel}</span>
				<form method="post" action="${pageContext.request.contextPath}/settlement"
					class="ml-auto"
					onsubmit="return confirm('Cancel this link?')">
					<input type="hidden" name="action" value="unlink"> <input
						type="hidden" name="txnId" value="${txn.id}"> <input
						type="hidden" name="linkId" value="${l.id}">
					<button type="submit" class="btn btn-outline btn-sm"
						style="color: var(--red)">&#10005; Cancel Link</button>
				</form>
			</div>
		</c:forEach>
	</div>

	<div class="card">
		<div class="card-title">&#43; Link another transaction</div>
		<form method="post"
			action="${pageContext.request.contextPath}/settlement">
			<input type="hidden" name="action" value="link"> <input
				type="hidden" name="txnId" value="${txn.id}">
			<div class="form-grid">
				<div class="form-group">
					<label>Transaction to link *</label> <select
						name="linkedTxnId" required>
						<option value="">Select&#8230;</option>
						<c:forEach var="c" items="${candidates}">
							<option value="${c.id}">#${c.id} &middot; ${c.formattedDate}
								&middot; &#8377;${c.amount}
								${not empty c.note ? '· ' += c.note : ''}</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>Amount (&#8377;) *</label> <input type="number"
						name="amount" step="0.01" min="0.01" required
						placeholder="Partial amount">
				</div>
			</div>
			<button type="submit" class="btn btn-primary">&#10003; Link</button>
		</form>
	</div>
</c:if>

<%@ include file="footer.jsp"%>
