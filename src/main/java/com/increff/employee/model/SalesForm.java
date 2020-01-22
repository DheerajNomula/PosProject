package com.increff.employee.model;

import java.util.Date;

public class SalesForm {
    private Date startDate;
    private Date endDate;
    private String brandName;
    private String brandCategory;

    public SalesForm() {
    }

    public SalesForm(Date startDate, Date endDate, String brandName, String brandCategory) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.brandName = brandName;
        this.brandCategory = brandCategory;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
        return "SalesForm{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", brandName='" + brandName + '\'' +
                ", brandCategory='" + brandCategory + '\'' +
                '}';
    }
}
