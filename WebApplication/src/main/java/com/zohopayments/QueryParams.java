package com.zohopayments;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

final class QueryParams {
   private final Map<String, String> params = new LinkedHashMap();

   QueryParams add(String key, String value) {
      Objects.requireNonNull(key, "query parameter key must not be null");
      if (value != null) {
         this.params.put(key, value);
      }

      return this;
   }

   QueryParams add(String key, Long value) {
      Objects.requireNonNull(key, "query parameter key must not be null");
      if (value != null) {
         this.params.put(key, value.toString());
      }

      return this;
   }

   QueryParams add(String key, Integer value) {
      Objects.requireNonNull(key, "query parameter key must not be null");
      if (value != null) {
         this.params.put(key, value.toString());
      }

      return this;
   }

   QueryParams add(String key, Boolean value) {
      Objects.requireNonNull(key, "query parameter key must not be null");
      if (value != null) {
         this.params.put(key, value.toString());
      }

      return this;
   }

   QueryParams addAll(QueryParams other) {
      if (other != null) {
         this.params.putAll(other.params);
      }

      return this;
   }

   String toQueryString() {
      if (this.params.isEmpty()) {
         return "";
      } else {
         StringBuilder sb = new StringBuilder();

         for(Map.Entry<String, String> entry : this.params.entrySet()) {
            if (sb.length() > 0) {
               sb.append("&");
            }

            sb.append(URLEncoder.encode((String)entry.getKey(), StandardCharsets.UTF_8));
            sb.append("=");
            sb.append(URLEncoder.encode((String)entry.getValue(), StandardCharsets.UTF_8));
         }

         return sb.toString();
      }
   }

   boolean isEmpty() {
      return this.params.isEmpty();
   }
}
