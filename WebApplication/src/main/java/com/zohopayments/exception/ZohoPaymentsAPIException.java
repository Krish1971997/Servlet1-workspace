package com.zohopayments.exception;

public class ZohoPaymentsAPIException extends ZohoPaymentsException {
   private static final long serialVersionUID = 1L;
   private final int httpStatusCode;
   private final String codeString;
   private final String errorMessage;

   public ZohoPaymentsAPIException(int httpStatusCode, String codeString, String errorMessage) {
      super("API error (HTTP " + httpStatusCode + "): code=" + codeString + ", message=" + errorMessage);
      this.httpStatusCode = httpStatusCode;
      this.codeString = codeString;
      this.errorMessage = errorMessage;
   }

   public int getHttpStatusCode() {
      return this.httpStatusCode;
   }

   public String getCodeString() {
      return this.codeString;
   }

   public String getApiErrorMessage() {
      return this.errorMessage;
   }
}
