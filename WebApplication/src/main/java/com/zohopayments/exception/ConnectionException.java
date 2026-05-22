package com.zohopayments.exception;

public class ConnectionException extends ZohoPaymentsException {
   private static final long serialVersionUID = 1L;

   public ConnectionException(String message) {
      super(message);
   }

   public ConnectionException(String message, Throwable cause) {
      super(message, cause);
   }
}
