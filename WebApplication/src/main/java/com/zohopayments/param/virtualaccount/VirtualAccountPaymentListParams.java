package com.zohopayments.param.virtualaccount;

import com.zohopayments.param.PaginationParams;

public final class VirtualAccountPaymentListParams implements PaginationParams {
   private final String status;
   private final Integer perPage;
   private final Integer page;
   private final String sortColumn;
   private final String sortOrder;

   private VirtualAccountPaymentListParams(Builder b) {
      this.status = b.status;
      this.perPage = b.perPage;
      this.page = b.page;
      this.sortColumn = b.sortColumn;
      this.sortOrder = b.sortOrder;
   }

   public String getStatus() {
      return this.status;
   }

   public Integer getPerPage() {
      return this.perPage;
   }

   public Integer getPage() {
      return this.page;
   }

   public String getSortColumn() {
      return this.sortColumn;
   }

   public String getSortOrder() {
      return this.sortOrder;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private String status;
      private Integer perPage;
      private Integer page;
      private String sortColumn;
      private String sortOrder;

      private Builder() {
      }

      public Builder status(String status) {
         this.status = status;
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

      public Builder sortColumn(String sortColumn) {
         this.sortColumn = sortColumn;
         return this;
      }

      public Builder sortOrder(String sortOrder) {
         this.sortOrder = sortOrder;
         return this;
      }

      public VirtualAccountPaymentListParams build() {
         return new VirtualAccountPaymentListParams(this);
      }
   }
}
