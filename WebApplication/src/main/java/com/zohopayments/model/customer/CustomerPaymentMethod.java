package com.zohopayments.model.customer;

public final class CustomerPaymentMethod {
	private String paymentMethodId;
	private String type;
	private String brand;
	private String lastFourDigits;
	private String expiryMonth;
	private String expiryYear;
	private Card card;
	private AchDebit achDebit;

	CustomerPaymentMethod() {
	}

	public String getPaymentMethodId() {
		return this.paymentMethodId;
	}

	public String getType() {
		return this.type;
	}

	public String getBrand() {
		return this.brand;
	}

	public String getLastFourDigits() {
		return this.lastFourDigits;
	}

	public String getExpiryMonth() {
		return this.expiryMonth;
	}

	public String getExpiryYear() {
		return this.expiryYear;
	}

	public Card getCard() {
		return this.card;
	}

	public AchDebit getAchDebit() {
		return this.achDebit;
	}

	public static final class Card {
		private String cardHolderName;
		private String lastFourDigits;
		private String expiryMonth;
		private String expiryYear;

		Card() {
		}

		public String getCardHolderName() {
			return this.cardHolderName;
		}

		public String getLastFourDigits() {
			return this.lastFourDigits;
		}

		public String getExpiryMonth() {
			return this.expiryMonth;
		}

		public String getExpiryYear() {
			return this.expiryYear;
		}
	}

	public static final class AchDebit {
		private String accountHolderName;
		private String lastFourDigits;
		private String accountHolderType;
		private String accountType;
		private String bankName;
		private String routingNumber;

		AchDebit() {
		}

		public String getAccountHolderName() {
			return this.accountHolderName;
		}

		public String getLastFourDigits() {
			return this.lastFourDigits;
		}

		public String getAccountHolderType() {
			return this.accountHolderType;
		}

		public String getAccountType() {
			return this.accountType;
		}

		public String getBankName() {
			return this.bankName;
		}

		public String getRoutingNumber() {
			return this.routingNumber;
		}
	}
}
