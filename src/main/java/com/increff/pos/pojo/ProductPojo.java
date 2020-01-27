package com.increff.pos.pojo;

import javax.persistence.*;

@Entity
@Table(indexes = {@Index(columnList = "brandId")})
public class ProductPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true) // it will also create index on it
    private String barcode;
    private String productName;
    private double mrp;
    private int brandId;

    public ProductPojo() {
    }

    public ProductPojo(String barcode, String productName, double mrp, int brandId) {
        this.barcode = barcode;
        this.productName = productName;
        this.mrp = mrp;
        this.brandId = brandId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

}
