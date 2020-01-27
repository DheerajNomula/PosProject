package com.increff.pos.model;

import com.sun.istack.NotNull;

public class BrandForm {
    @NotNull
    private String brandName;
    @NotNull
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

}
