package com.zohopayments.model;

public final class PageContext {
   private int page;
   private int perPage;
   private int total;
   private int totalPages;
   private boolean hasMorePage;

   PageContext() {
   }

   public int getPage() {
      return this.page;
   }

   public int getPerPage() {
      return this.perPage;
   }

   public int getTotal() {
      return this.total;
   }

   public int getTotalPages() {
      return this.totalPages;
   }

   public boolean hasMorePage() {
      return this.hasMorePage;
   }
}
