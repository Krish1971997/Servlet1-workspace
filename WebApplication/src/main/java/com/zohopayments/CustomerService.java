package com.zohopayments;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.zohopayments.model.ListResponse;
import com.zohopayments.model.customer.Customer;
import com.zohopayments.model.customer.CustomerSummary;
import com.zohopayments.param.customer.CustomerCreateParams;
import com.zohopayments.param.customer.CustomerListParams;
import java.lang.reflect.Type;
import java.util.List;

public final class CustomerService {
   private static final Type CUSTOMER_LIST_TYPE = (new TypeToken<List<CustomerSummary>>() {
   }).getType();
   private final ZohoHttpClient client;
   private final Edition edition;

   CustomerService(ZohoHttpClient client, Edition edition) {
      this.client = client;
      this.edition = edition;
   }

   public Customer create(CustomerCreateParams params) {
      if (params == null) {
         throw new IllegalArgumentException("params is required");
      } else {
         JsonObject body = JsonUtil.gson().toJsonTree(params).getAsJsonObject();
         return (Customer)this.client.post("/customers", body, Customer.class, "customer");
      }
   }

   public Customer get(String customerId) {
      if (customerId != null && !customerId.isEmpty()) {
         return (Customer)this.client.get("/customers/" + ZohoHttpClient.encodePath(customerId), Customer.class, "customer");
      } else {
         throw new IllegalArgumentException("customerId is required");
      }
   }

   public ListResponse<CustomerSummary> list() {
      return this.list((CustomerListParams)null);
   }

   public ListResponse<CustomerSummary> list(CustomerListParams params) {
      if (!this.edition.isUS()) {
         throw new UnsupportedOperationException("Customer list is only available for Edition.US");
      } else {
         QueryParams q = null;
         if (params != null) {
            q = (new QueryParams()).add("filter_by", params.getFilterBy()).add("from_date", params.getFromDate()).add("to_date", params.getToDate()).add("per_page", params.getPerPage()).add("page", params.getPage());
         }

         return this.client.<CustomerSummary>list("/customers", q, CUSTOMER_LIST_TYPE, "customers");
      }
   }

   public void delete(String customerId) {
      if (!this.edition.isUS()) {
         throw new UnsupportedOperationException("Customer delete is only available for Edition.US");
      } else if (customerId != null && !customerId.isEmpty()) {
         this.client.delete("/customers/" + ZohoHttpClient.encodePath(customerId));
      } else {
         throw new IllegalArgumentException("customerId is required");
      }
   }
}
