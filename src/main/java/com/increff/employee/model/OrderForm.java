package com.increff.employee.model;

import java.util.Date;

public class OrderForm {
    // barcode,date,quantity,mrp of each
    private String barcode;
    private int quantity;

    public OrderForm() {
    }

    public OrderForm(String barcode, int quantity) {
        this.barcode = barcode;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
