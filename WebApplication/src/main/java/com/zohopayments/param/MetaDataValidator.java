package com.zohopayments.param;

import java.util.List;

public final class MetaDataValidator {
   private static final int MAX_ENTRIES = 5;
   private static final int MAX_KEY_LENGTH = 20;
   private static final int MAX_VALUE_LENGTH = 500;

   private MetaDataValidator() {
   }

   public static void validate(List<MetaDataParams> metaData) {
      if (metaData != null) {
         if (metaData.size() > 5) {
            throw new IllegalArgumentException("metaData must have at most 5 entries");
         } else {
            for(MetaDataParams entry : metaData) {
               if (entry == null) {
                  throw new IllegalArgumentException("metaData entries must not be null");
               }

               String k = entry.getKey();
               String v = entry.getValue();
               if (k == null || k.isEmpty()) {
                  throw new IllegalArgumentException("metaData key must not be null or empty");
               }

               if (k.length() > 20) {
                  throw new IllegalArgumentException("metaData key must be at most 20 characters");
               }

               if (v != null && v.length() > 500) {
                  throw new IllegalArgumentException("metaData value must be at most 500 characters");
               }
            }

         }
      }
   }
}
