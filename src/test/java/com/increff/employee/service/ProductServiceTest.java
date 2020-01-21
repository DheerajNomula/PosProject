package com.increff.employee.service;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

public class ProductServiceTest extends AbstractUnitTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    @Before
    public void init() throws ApiException {
        BrandPojo brandPojo=new BrandPojo("lenovo","laptops");
        brandService.add(brandPojo);
    }

    @Test
    public void  testNormalize() throws ApiException {
        ProductPojo productPojo=new ProductPojo(" LeNovo123 ","thiNkPad",25000.00,1);
        productService.normalize(productPojo);
        Assert.assertEquals("lenovo123",productPojo.getBarcode());
        Assert.assertEquals("thinkpad",productPojo.getProductName());
    }

    @Test
    public void  testNormalize_null() throws ApiException {
        ProductPojo productPojo=new ProductPojo(" ","thiNkPad",25000.00,1);
        productService.normalize(productPojo);
        Assert.assertEquals("",productPojo.getBarcode());
        Assert.assertEquals("thinkpad",productPojo.getProductName());
    }

    @Test
    public void  testAdd() throws ApiException { //valid add
        ProductPojo productPojo=new ProductPojo(" LeNovo123 ","thiNkPad",25000.00,1);
        productService.add(productPojo);
        Assert.assertEquals(1,productPojo.getId());
    }

    @Test(expected = ApiException.class)
    public void  testAdd_null_barcode() throws ApiException { //invalid(barcode) add
        ProductPojo productPojo=new ProductPojo("   ","thiNkPad",25000.00,1);
        productService.add(productPojo);
        Assert.assertEquals(1,productPojo.getId());
    }

    @Test(expected = ApiException.class)
    public void  testAdd_null_name() throws ApiException { //invalid (name) add
        ProductPojo productPojo=new ProductPojo(" LeNovo123 "," ",25000.00,1);
        productService.add(productPojo);
        Assert.assertEquals(1,productPojo.getId());
    }

    @Test(expected = ApiException.class)
    public void  testAdd_invalid_mrp() throws ApiException {//invalid (mrp) add
        ProductPojo productPojo=new ProductPojo(" LeNovo123 "," ",25000.00,1);
        productService.add(productPojo);
        Assert.assertEquals(1,productPojo.getId());
    }
}
