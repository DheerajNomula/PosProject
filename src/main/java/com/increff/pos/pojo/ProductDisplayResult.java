package com.increff.pos.pojo;

public class ProductDisplayResult {
    private int id;
    private String brandName;
    private String brandCategory;
    private String barcode;
    private String productName;
    private double mrp;

    public ProductDisplayResult(int id, String brandName, String brandCategory, String barcode, String productName, double mrp) {
        this.id = id;
        this.brandName = brandName;
        this.brandCategory = brandCategory;
        this.barcode = barcode;
        this.productName = productName;
        this.mrp = mrp;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
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


}
