package com.increff.pos.pojo;

public class InventoryReportResult {
    private String brandName;
    private String brandCategory;
    private long quantity;

    public InventoryReportResult(String brandName, String brandCategory, long quantity) {
        this.brandName = brandName;
        this.brandCategory = brandCategory;
        this.quantity = quantity;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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
}
