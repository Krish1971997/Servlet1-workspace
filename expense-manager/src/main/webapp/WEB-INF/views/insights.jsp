<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="pageTitle" value="Insights" scope="request" />
<c:set var="activePage" value="insights" scope="request" />
<%@ include file="header.jsp"%>

<style>
.ins-hero { background: linear-gradient(135deg, #2563eb, #7c3aed); color: #fff;
	border-radius: 14px; padding: 1.25rem 1.5rem; margin-bottom: 1rem; }
.ins-hero .big { font-size: 2rem; font-weight: 800; }
.ins-bars .bar-track { background: #eef2f7; border-radius: 4px; height: 8px; overflow: hidden; }
.ins-bars .bar-fill { height: 100%; border-radius: 4px; }
.top-cards { display: grid; grid-template-columns: repeat(3, 1fr); gap: .75rem; margin-bottom: 1rem; }
@media (max-width: 720px) { .top-cards { grid-template-columns: 1fr; } }
.top-card { background: #fff; border: 1px solid var(--border); border-radius: 10px; padding: .8rem; }
</style>

<div class="page-header" style="margin-bottom: 1rem">
	<h2>&#128161; Insights</h2>
	<div class="tabs" style="margin-top: .5rem">
		<a class="tab ${type=='EXPENSE'?'active':''}"
			href="${pageContext.request.contextPath}/insights?${prevNav}&type=EXPENSE">&#9664;</a>
		<span class="tab active">${monthLabel}</span>
		<a class="tab ${type=='EXPENSE'?'active':''}"
			href="${pageContext.request.contextPath}/insights?${nextNav}&type=EXPENSE">&#9654;</a>
		<a class="tab ${type=='EXPENSE'?'active expense':''}"
			href="${pageContext.request.contextPath}/insights?year=${year}&month=${month}&type=EXPENSE">Expenses</a>
		<a class="tab ${type=='INCOME'?'active income':''}"
			href="${pageContext.request.contextPath}/insights?year=${year}&month=${month}&type=INCOME">Income</a>
	</div>
</div>

<c:if test="${not empty dbError}">
	<div class="alert alert-error">&#10007; ${dbError}</div>
</c:if>

<div class="ins-hero">
	<div class="big">&#8377;<fmt:formatNumber value="${curTotal}" pattern="#,##0" /></div>
	<div style="font-size: .85rem; opacity: .85">${prevMonthLabel}:
		&#8377;<fmt:formatNumber value="${prevTotal}" pattern="#,##0" /></div>
	<div style="margin-top: .5rem; font-size: .9rem">${insight}</div>
</div>

<c:if test="${empty cats}">
	<div class="alert">No ${type == 'INCOME' ? 'income' : 'expenses'} recorded for this month yet.</div>
</c:if>

<c:if test="${not empty cats}">
	<%-- Top 3 highlight cards --%>
	<div class="top-cards">
		<c:forEach var="r" items="${cats}" begin="0" end="2">
			<div class="top-card">
				<div style="font-size: .72rem; color: var(--text-2)">${r.name}</div>
				<div style="font-size: 1.15rem; font-weight: 700; ${r.pct > 0 && status.index == 0 ? 'color: var(--red)' : ''}">&#8377;<fmt:formatNumber
						value="${r.total}" pattern="#,##0" /></div>
				<div style="font-size: .7rem; color: var(--text-2)">${r.pct}%
					of total</div>
			</div>
		</c:forEach>
	</div>

	<%-- Full category breakdown with proportional bars --%>
	<div class="card ins-bars">
		<c:forEach var="r" items="${cats}" varStatus="s">
			<div style="margin-bottom: .8rem">
				<div class="flex" style="align-items: center">
					<span style="flex: 1">${r.name}</span> <span
						style="font-size: .78rem; color: var(--text-2)">${r.pct}%</span>
					<strong style="margin-left: .6rem">&#8377;<fmt:formatNumber
							value="${r.total}" pattern="#,##0" /></strong>
				</div>
				<div class="bar-track" style="margin-top: .3rem">
					<div class="bar-fill" style="width: ${r.pct}%; background: ${s.index % 6 == 0 ? '#f59e0b' : s.index % 6 == 1 ? '#16a34a' : s.index % 6 == 2 ? '#dc2626' : s.index % 6 == 3 ? '#2563eb' : s.index % 6 == 4 ? '#7c3aed' : '#0891b2'}"></div>
				</div>
			</div>
		</c:forEach>
	</div>
</c:if>

<%@ include file="footer.jsp"%>
