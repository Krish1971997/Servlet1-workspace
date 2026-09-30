package com.expensemanager.model;

import java.math.BigDecimal;

/**
 * Ported from Android SettlementLinkDao.Link — links a "settlement"
 * transaction to one or more other transactions with a partial amount,
 * e.g. a lump-sum repayment linked against the individual expenses it
 * settles. Links are directional (settlement side → linked side) but
 * discoverable from either transaction.
 */
public class SettlementLink {
	private long id;
	private int settlementTxnId;
	private int linkedTxnId;
	private BigDecimal amount;

	public int otherSide(int txnId) {
		return settlementTxnId == txnId ? linkedTxnId : settlementTxnId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSettlementTxnId() {
		return settlementTxnId;
	}

	public void setSettlementTxnId(int settlementTxnId) {
		this.settlementTxnId = settlementTxnId;
	}

	public int getLinkedTxnId() {
		return linkedTxnId;
	}

	public void setLinkedTxnId(int linkedTxnId) {
		this.linkedTxnId = linkedTxnId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
