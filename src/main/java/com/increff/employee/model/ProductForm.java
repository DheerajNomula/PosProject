package com.increff.employee.model;

public class ProductForm {

    private String barcode;
    private int brandId;
    private String productName;
    private double mrp;

    public ProductForm() {
    }

    public ProductForm(String barcode, int brandId, String productName, double mrp) {
        this.barcode = barcode;
        this.brandId = brandId;
        this.productName = productName;
        this.mrp = mrp;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
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

    @Override
    public String toString() {
        return "ProductForm{" +
                "barcode='" + barcode + '\'' +
                ", brandId=" + brandId +
                ", productName='" + productName + '\'' +
                ", mrp=" + mrp +
                '}';
    }
}
