package com.increff.employee.model;

public class InventoryData extends InventoryForm {
    private String productName;
    private String brandCategory;
    private String brandName;
    private int id;

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



    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "InventoryData{" +
                "productName='" + productName + '\'' +
                ", brandCategory='" + brandCategory + '\'' +
                ", brandName='" + brandName + '\'' +
                ", id=" + id +
                '}';
    }
}
