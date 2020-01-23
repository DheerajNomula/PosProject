package com.increff.employee.model;

public class BrandData extends BrandForm {
    private int id;

    public BrandData() {
    }

    public BrandData(String brandName, String brandCategory, int id) {
        super(brandName, brandCategory);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
