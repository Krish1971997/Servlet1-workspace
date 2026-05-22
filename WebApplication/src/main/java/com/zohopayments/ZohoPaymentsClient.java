package com.zohopayments;

public final class ZohoPaymentsClient implements AutoCloseable {
	private final Edition edition;
	private final TokenManager tokenManager;
	private final ZohoHttpClient httpClient;
	private final PaymentLinkService paymentLinks;
	private final PaymentSessionService paymentSessions;
	private final CustomerService customers;
	private final PaymentService payments;
	private final RefundService refunds;
	private final PaymentMethodSessionService paymentMethodSessions;
	private final PaymentMethodService paymentMethods;
	private final MandateService mandates;
	private final CollectService collect;

	ZohoPaymentsClient(ZohoHttpClient httpClient, TokenManager tokenManager, Edition edition) {
		this.edition = edition;
		this.tokenManager = tokenManager;
		this.httpClient = httpClient;
		this.paymentLinks = new PaymentLinkService(httpClient);
		this.paymentSessions = new PaymentSessionService(httpClient);
		this.customers = new CustomerService(httpClient, edition);
		this.payments = new PaymentService(httpClient, edition);
		this.refunds = new RefundService(httpClient);
		this.mandates = new MandateService(httpClient);
		this.collect = new CollectService(httpClient);
		this.paymentMethods = new PaymentMethodService(httpClient);
		this.paymentMethodSessions = new PaymentMethodSessionService(httpClient);
	}

	public MandateService mandates() {
		if (!this.edition.isIN()) {
			throw new UnsupportedOperationException("MandateService is only available for Edition.IN");
		} else {
			return this.mandates;
		}
	}

	public CollectService collect() {
		if (!this.edition.isIN()) {
			throw new UnsupportedOperationException("CollectService is only available for Edition.IN");
		} else {
			return this.collect;
		}
	}

	public PaymentMethodService paymentMethods() {
		if (!this.edition.isUS()) {
			throw new UnsupportedOperationException("PaymentMethodService is only available for Edition.US");
		} else {
			return this.paymentMethods;
		}
	}

	public PaymentMethodSessionService paymentMethodSessions() {
		if (!this.edition.isUS()) {
			throw new UnsupportedOperationException("PaymentMethodSessionService is only available for Edition.US");
		} else {
			return this.paymentMethodSessions;
		}
	}

	public void updateToken(String newAccessToken) {
		this.tokenManager.updateToken(newAccessToken);
	}

	public PaymentLinkService paymentLinks() {
		return this.paymentLinks;
	}

	public PaymentSessionService paymentSessions() {
		return this.paymentSessions;
	}

	public CustomerService customers() {
		return this.customers;
	}

	public PaymentService payments() {
		return this.payments;
	}

	public RefundService refunds() {
		return this.refunds;
	}

	public void close() {
		this.httpClient.close();
	}
}
