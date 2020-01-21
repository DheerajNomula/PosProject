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


    private ProductData convert(ProductPojo p) throws ApiException {
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

    private static ProductPojo convert(ProductForm f) {
        ProductPojo p = new ProductPojo();
        p.setMrp(f.getMrp());
        p.setBrandId(f.getBrandId());
        p.setBarcode(f.getBarcode());
        p.setProductName(f.getProductName());
        return p;
    }

    private boolean checkBrand(ProductPojo p) throws ApiException {

        List<BrandPojo> brandPojos=brandService.getAll();
        for(BrandPojo brandPojo:brandPojos){
            if(brandPojo.getId()==p.getBrandId())
                return true;
        }
        throw new ApiException("Brand with id:"+p.getBrandId()+" doestn't exist ");
    }

    public BrandPojo getBrand(int brandId) throws ApiException {
        BrandPojo brandPojo=brandService.get(brandId);
        return brandPojo;
    }

}
