package com.increff.employee.model;

public class BrandForm {
    private String brandName;
    private String brandCategory;

    public BrandForm() {
    }

    public BrandForm(String brandName, String brandCategory) {
        this.brandName = brandName;
        this.brandCategory = brandCategory;
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

    @Override
    public String toString() {
        return "BrandForm{" +
                "brandName='" + brandName + '\'' +
                ", brandCategory='" + brandCategory + '\'' +
                '}';
    }
}
