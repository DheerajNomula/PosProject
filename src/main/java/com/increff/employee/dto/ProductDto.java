package com.increff.employee.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductDto {
    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    public void add( ProductForm form) throws ApiException {
        ProductPojo productPojo = convert(form);
        if(checkBrand(productPojo)){
            productService.add(productPojo);
        }

    }

    public ProductData get( int id) throws ApiException {
        ProductPojo p = productService.get(id);
        return convert(p);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> list = productService.getAll();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for (ProductPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    public void update( int id,  ProductForm f) throws ApiException {
        ProductPojo productPojo = convert(f);
        checkBrand(productPojo);
        productService.update(id, productPojo);
    }


    protected ProductData convert(ProductPojo p) throws ApiException {
        ProductData d = new ProductData();
        d.setId(p.getId());
        d.setBarcode(p.getBarcode());
        BrandPojo brandPojo=getBrand(p.getBrandId());
        d.setBrandName(brandPojo.getBrandName());
        d.setBrandCategory(brandPojo.getBrandCategory());
        d.setMrp(p.getMrp());
        d.setProductName(p.getProductName());
        return d;
    }

    protected static ProductPojo convert(ProductForm f) {
        ProductPojo p = new ProductPojo();
        p.setMrp(f.getMrp());
        p.setBrandId(f.getBrandId());
        p.setBarcode(f.getBarcode());
        p.setProductName(f.getProductName());
        return p;
    }

    protected boolean checkBrand(ProductPojo p) {
        try {
            BrandPojo brandPojo=brandService.get(p.getBrandId());
        } catch (ApiException e) {
            return false;
        }
        return true;
    }

    protected BrandPojo getBrand(int brandId) throws ApiException {
        BrandPojo brandPojo=brandService.get(brandId);
        return brandPojo;
    }

}
