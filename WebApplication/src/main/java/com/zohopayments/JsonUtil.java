package com.zohopayments;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.zohopayments.exception.ZohoPaymentsException;
import com.zohopayments.model.PageContext;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class JsonUtil {
   private static final JsonDeserializer<BigDecimal> BIG_DECIMAL_LENIENT = (json, typeOfT, context) -> {
      if (json != null && !json.isJsonNull()) {
         if (json.isJsonPrimitive()) {
            JsonPrimitive p = json.getAsJsonPrimitive();
            if (p.isString()) {
               String s = p.getAsString();
               if (s != null && !s.isBlank()) {
                  return new BigDecimal(s.trim());
               }

               return null;
            }
         }

         try {
            return json.getAsBigDecimal();
         } catch (NumberFormatException var5) {
            return null;
         }
      } else {
         return null;
      }
   };
   private static final Gson GSON;

   private JsonUtil() {
   }

   static Gson gson() {
      return GSON;
   }

   static String toJson(Object obj) {
      return GSON.toJson(obj);
   }

   static <T> T fromJson(String json, Class<T> clazz) {
      return (T)GSON.fromJson(json, clazz);
   }

   static <T> T fromJson(JsonElement json, Class<T> clazz) {
      return (T)GSON.fromJson(json, clazz);
   }

   static <T> T fromJson(String json, Type type) {
      return (T)GSON.fromJson(json, type);
   }

   static <T> T fromJson(JsonElement json, Type type) {
      return (T)GSON.fromJson(json, type);
   }

   static JsonObject parseObject(String json) {
      return (JsonObject)GSON.fromJson(json, JsonObject.class);
   }

   static <T> List<T> listFromJson(JsonObject body, Type type, String... keys) {
      if (body == null) {
         return Collections.emptyList();
      } else {
         for(String key : keys) {
            if (body.has(key) && body.get(key).isJsonArray()) {
               List<T> result = (List)GSON.fromJson(body.getAsJsonArray(key), type);
               return result != null ? result : Collections.emptyList();
            }
         }

         return Collections.emptyList();
      }
   }

   static JsonObject getObject(JsonObject body, String... keys) {
      if (body == null) {
         return null;
      } else {
         for(String key : keys) {
            if (body.has(key) && body.get(key).isJsonObject()) {
               return body.getAsJsonObject(key);
            }
         }

         return null;
      }
   }

   static JsonObject getObjectRequired(JsonObject body, String... candidateKeys) {
      if (body == null) {
         throw new ZohoPaymentsException("Response body is null; expected resource object with key(s) " + Arrays.toString(candidateKeys));
      } else {
         for(String key : candidateKeys) {
            if (body.has(key) && body.get(key).isJsonObject()) {
               return body.getAsJsonObject(key);
            }
         }

         throw new ZohoPaymentsException("Response body missing expected resource key(s) " + Arrays.toString(candidateKeys));
      }
   }

   static <T> T unwrap(ApiResponse response, Class<T> type, String... candidateKeys) {
      return (T)fromJson((JsonElement)getObjectRequired(response.getBody(), candidateKeys), type);
   }

   static PageContext readPageContext(JsonObject body) {
      JsonObject pageObj = getObject(body, "page_context");
      return pageObj == null ? null : (PageContext)fromJson((JsonElement)pageObj, PageContext.class);
   }

   static {
      GSON = (new GsonBuilder()).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).registerTypeAdapter(BigDecimal.class, BIG_DECIMAL_LENIENT).create();
   }
}
