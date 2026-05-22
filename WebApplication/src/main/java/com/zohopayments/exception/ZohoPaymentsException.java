package com.zohopayments.exception;

public class ZohoPaymentsException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public ZohoPaymentsException(String message) {
      super(message);
   }

   public ZohoPaymentsException(String message, Throwable cause) {
      super(message, cause);
   }
}
