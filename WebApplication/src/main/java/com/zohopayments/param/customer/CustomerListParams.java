package com.zohopayments.param.customer;

import com.zohopayments.param.PaginationParams;

public final class CustomerListParams implements PaginationParams {
   private final String filterBy;
   private final String fromDate;
   private final String toDate;
   private final Integer perPage;
   private final Integer page;

   private CustomerListParams(Builder b) {
      this.filterBy = b.filterBy;
      this.fromDate = b.fromDate;
      this.toDate = b.toDate;
      this.perPage = b.perPage;
      this.page = b.page;
   }

   public String getFilterBy() {
      return this.filterBy;
   }

   public String getFromDate() {
      return this.fromDate;
   }

   public String getToDate() {
      return this.toDate;
   }

   public Integer getPerPage() {
      return this.perPage;
   }

   public Integer getPage() {
      return this.page;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private String filterBy;
      private String fromDate;
      private String toDate;
      private Integer perPage;
      private Integer page;

      private Builder() {
      }

      public Builder filterBy(String filterBy) {
         this.filterBy = filterBy;
         return this;
      }

      public Builder fromDate(String fromDate) {
         this.fromDate = fromDate;
         return this;
      }

      public Builder toDate(String toDate) {
         this.toDate = toDate;
         return this;
      }

      public Builder perPage(Integer perPage) {
         this.perPage = perPage;
         return this;
      }

      public Builder page(Integer page) {
         this.page = page;
         return this;
      }

      public CustomerListParams build() {
         return new CustomerListParams(this);
      }
   }
}
