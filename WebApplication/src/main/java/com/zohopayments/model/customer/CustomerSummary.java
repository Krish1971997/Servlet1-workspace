package com.zohopayments.model.customer;

public final class CustomerSummary {
	private String customerId;
	private String customerName;
	private String customerEmail;
	private String customerPhone;
	private String customerStatus;
	private Long createdTime;
	private Long lastModifiedTime;

	CustomerSummary() {
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public String getCustomerEmail() {
		return this.customerEmail;
	}

	public String getCustomerPhone() {
		return this.customerPhone;
	}

	public String getCustomerStatus() {
		return this.customerStatus;
	}

	public Long getCreatedTime() {
		return this.createdTime;
	}

	public Long getLastModifiedTime() {
		return this.lastModifiedTime;
	}
}
