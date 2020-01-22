package com.increff.employee.model;

public class InventoryForm {
    private String barcode;
    private int quantity;

    public InventoryForm(String barcode, int quantity) {
        this.barcode = barcode;
        this.quantity = quantity;
    }

    public InventoryForm() {
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

    @Override
    public String toString() {
        return "InventoryForm{" +
                "barcode=" + barcode +
                ", quantity=" + quantity +
                '}';
    }
}
