<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="pageTitle" value="Food Tracker" scope="request" />
<c:set var="activePage" value="food" scope="request" />
<%@ include file="header.jsp"%>

<style>
.food-grid { border-collapse: collapse; width: 100%; }
.food-grid th, .food-grid td { border: 1px solid var(--border); padding: .35rem .45rem; text-align: center; }
.food-grid input { width: 90px; text-align: right; padding: .3rem .4rem; }
.meal-total { font-weight: 700; color: var(--red); }
</style>

<div class="page-header" style="margin-bottom: 1rem">
	<h2>&#127869; Food Tracker &mdash; ${monthLabel}</h2>
	<div class="flex gap-1" style="margin-top: .5rem">
		<a class="btn btn-outline btn-sm"
			href="${pageContext.request.contextPath}/foodtracker?${prevNav}">&#9664;
			Prev</a> <a class="btn btn-outline btn-sm"
			href="${pageContext.request.contextPath}/foodtracker?${nextNav}">Next
			&#9654;</a>
	</div>
	<p style="font-size: .8rem; color: var(--text-2); margin-top: .4rem">
		Auto-saves each cell into this month's <strong>${monthLabel} Food</strong>
		book (Breakfast 9:00 AM, Lunch 1:00 PM, Dinner 8:30 PM, payment type
		&ldquo;Cash&rdquo;). Clearing a value back to 0 removes the entry.</p>
</div>

<c:if test="${not empty dbError}">
	<div class="alert alert-error">&#10007; ${dbError}</div>
</c:if>

<div class="card" style="padding: 0; overflow-x: auto">
	<table class="food-grid">
		<thead>
			<tr>
				<th>Date</th>
				<c:forEach var="m" items="${meals}">
					<th>${m}</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="d" items="${days}">
				<tr>
					<td style="text-align: left">${d.label}</td>
					<c:forEach var="m" items="${meals}">
						<td>
							<c:set var="val" value="${d.meals.get(m)}" />
							<form method="post"
								action="${pageContext.request.contextPath}/foodtracker"
								style="display: inline">
								<input type="hidden" name="action" value="save"> <input
									type="hidden" name="ym" value="${ymParam}"> <input
									type="hidden" name="date" value="${d.dateStr}"> <input
									type="hidden" name="meal" value="${m}"> <input
									type="number" name="amount" step="0.01" min="0"
									value="${not empty val ? val : ''}" placeholder="0.00"
									onchange="this.form.submit()">
							</form>
						</td>
					</c:forEach>
				</tr>
			</c:forEach>
			<tr>
				<td style="text-align: left"><strong>Total</strong></td>
				<c:forEach var="m" items="${meals}">
					<td class="meal-total">&#8377;<fmt:formatNumber
							value="${mealTotals.get(m)}" pattern="#,##0.00" /></td>
				</c:forEach>
			</tr>
		</tbody>
	</table>
</div>

<div class="mt-2" style="font-size: .9rem">
	Month total: <strong>&#8377;<fmt:formatNumber value="${monthTotal}"
			pattern="#,##0.00" /></strong>
</div>

<%@ include file="footer.jsp"%>
