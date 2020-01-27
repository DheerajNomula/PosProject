package com.increff.pos.dto;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductDisplayResult;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ProductDto {
    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    protected static ProductPojo convert(ProductForm f) {
        ProductPojo p = new ProductPojo();
        p.setMrp(f.getMrp());
        p.setBrandId(f.getBrandId());
        p.setBarcode(f.getBarcode());
        p.setProductName(f.getProductName());
        return p;
    }

    public void add(ProductForm form) throws ApiException {
        ProductPojo productPojo = convert(form);
        checkBrand(productPojo);
        productService.add(productPojo);
    }

    public ProductData get(int id) throws ApiException {
        ProductPojo p = productService.getCheck(id);
        return convert(p);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductDisplayResult> list = productService.getAll();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for (ProductDisplayResult p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    public void update(int id, ProductForm f) throws ApiException {
        ProductPojo productPojo = convert(f);
        checkBrand(productPojo);
        productService.update(id, productPojo);
    }

    protected ProductData convert(ProductDisplayResult p){
        ProductData d = new ProductData();
        d.setId(p.getId());
        d.setBarcode(p.getBarcode());
        d.setBrandName(p.getBrandName());
        d.setBrandCategory(p.getBrandCategory());
        d.setMrp(p.getMrp());
        d.setProductName(p.getProductName());
        return d;
    }

    protected ProductData convert(ProductPojo p) throws ApiException {
        ProductData d = new ProductData();
        d.setId(p.getId());
        d.setBarcode(p.getBarcode());
        BrandPojo brandPojo = getBrand(p.getBrandId());
        d.setBrandName(brandPojo.getBrandName());
        d.setBrandCategory(brandPojo.getBrandCategory());
        d.setMrp(p.getMrp());
        d.setProductName(p.getProductName());
        return d;
    }

    protected void checkBrand(ProductPojo p) throws ApiException {
        try {
            BrandPojo brandPojo = brandService.getCheck(p.getBrandId());
        } catch (ApiException e) {
            throw new ApiException("Brand details not found");
        }
    }

    protected BrandPojo getBrand(int brandId) throws ApiException {
        BrandPojo brandPojo = brandService.getCheck(brandId);
        return brandPojo;
    }

}
