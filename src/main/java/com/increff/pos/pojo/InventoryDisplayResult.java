package com.increff.pos.pojo;

public class InventoryDisplayResult
{
    private String barcode;
    private int quantity;
    private String productName;
    private String brandCategory;
    private String brandName;
    private int id;

    public InventoryDisplayResult(String barcode, int quantity, String productName, String brandCategory, String brandName, int id) {
        this.barcode = barcode;
        this.quantity = quantity;
        this.productName = productName;
        this.brandCategory = brandCategory;
        this.brandName = brandName;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}
