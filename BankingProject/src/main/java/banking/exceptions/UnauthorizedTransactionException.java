package banking.exceptions;

public class UnauthorizedTransactionException extends Exception {
	public UnauthorizedTransactionException(String message) {
		super(message);
	}
}
