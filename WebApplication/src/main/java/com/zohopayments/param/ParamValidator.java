package com.zohopayments.param;

public final class ParamValidator {
   static final int MAX_DESCRIPTION_LENGTH = 500;
   static final int MAX_INVOICE_NUMBER_LENGTH = 50;
   static final int MAX_REFERENCE_LENGTH = 50;

   private ParamValidator() {
   }

   public static void validateDescription(String description) {
      if (description != null && description.length() > 500) {
         throw new IllegalArgumentException("description must be at most 500 characters");
      }
   }

   public static void validateInvoiceNumber(String invoiceNumber) {
      if (invoiceNumber != null && invoiceNumber.length() > 50) {
         throw new IllegalArgumentException("invoiceNumber must be at most 50 characters");
      }
   }

   public static void validateReferenceNumber(String referenceNumber) {
      if (referenceNumber != null && referenceNumber.length() > 50) {
         throw new IllegalArgumentException("referenceNumber must be at most 50 characters");
      }
   }
}
