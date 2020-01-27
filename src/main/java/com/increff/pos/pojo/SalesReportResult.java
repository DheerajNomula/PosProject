package com.increff.pos.pojo;

public class SalesReportResult {
    private String brandCategory;
    private long quantity;
    private double sellingPrice;

    public SalesReportResult(String brandCategory, long quantity, double sellingPrice) {
        this.brandCategory = brandCategory;
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
    }

    public String getBrandCategory() {
        return brandCategory;
    }

    public void setBrandCategory(String brandCategory) {
        this.brandCategory = brandCategory;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
