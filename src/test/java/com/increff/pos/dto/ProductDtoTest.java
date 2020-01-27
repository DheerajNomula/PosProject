package com.increff.pos.dto;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ProductDtoTest extends AbstractUnitTest {

    @Autowired
    private ProductDto productDto;

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    private BrandPojo brandPojo;

    @Before
    public void init() throws ApiException {
        brandPojo = new BrandPojo("lenovo", "laptops");
        brandService.add(brandPojo);
    }

    private void checkPojoAndForm(ProductForm productForm, ProductPojo productPojo) {
        Assert.assertEquals(productForm.getBarcode(), productPojo.getBarcode());
        Assert.assertEquals(productForm.getBrandId(), productPojo.getBrandId());
        Assert.assertEquals(productForm.getMrp(), productPojo.getMrp(), 0.0f);
        Assert.assertEquals(productForm.getProductName(), productPojo.getProductName());
    }

    private void checkPojoAndData(ProductPojo productPojo, ProductData productData) {
        Assert.assertEquals(productPojo.getBarcode(), productData.getBarcode());
        Assert.assertEquals(brandPojo.getBrandCategory(), productData.getBrandCategory());
        Assert.assertEquals(brandPojo.getBrandName(), productData.getBrandName());
        Assert.assertEquals(productPojo.getMrp(), productData.getMrp(), 0.0f);
        Assert.assertEquals(productPojo.getProductName(), productData.getProductName());
    }

    @Test
    public void testConvertForm() {
        ProductForm productForm = new ProductForm("laptop123", brandPojo.getId(), "thinkpad", 25000);
        ProductPojo productPojo = ProductDto.convert(productForm);
        checkPojoAndForm(productForm, productPojo);
    }

    @Test
    public void testConvertPojo() throws ApiException {
        ProductPojo productPojo = new ProductPojo("laptop123", "thinkpad", 25000, brandPojo.getId());
        ProductData productData = productDto.convert(productPojo);
        checkPojoAndData(productPojo, productData);
    }

    @Test
    public void testAdd_valid() throws ApiException {
        ProductForm productForm = new ProductForm("laptop123", brandPojo.getId(), "thinkpad", 25000);
        productDto.add(productForm);

        ProductPojo productPojo = productService.getProductByBarcode("laptop123");
        checkPojoAndForm(productForm, productPojo);
    }

    @Test(expected = ApiException.class)
    public void testAdd_invalid_brand() throws ApiException {
        ProductForm productForm = new ProductForm("laptop123", brandPojo.getId() + 20, "thinkpad", 25000);
        productDto.add(productForm);
    }

    @Test(expected = ApiException.class)
    public void testAdd_existing_barcode() throws ApiException {
        ProductForm productForm = new ProductForm("laptop123", brandPojo.getId(), "thinkpad", 25000);
        productDto.add(productForm);
        ProductForm productForm1 = new ProductForm("laptop123", brandPojo.getId(), "thinkpad", 25000);
        productDto.add(productForm);
    }

    //all other check methods are checked in
    @Test
    public void testGet_valid() throws ApiException {
        ProductPojo productPojo = new ProductPojo("laptop123", "thinkpad", 25000, brandPojo.getId());
        productService.add(productPojo);

        ProductData productData = productDto.get(productPojo.getId());
        checkPojoAndData(productPojo, productData);
    }

    @Test(expected = ApiException.class)
    public void testGet_invalid() throws ApiException {
        ProductPojo productPojo = new ProductPojo("laptop123", "thinkpad", 25000, brandPojo.getId());
        productService.add(productPojo);
        productDto.get(productPojo.getId() + 10);
    }

    @Test
    public void testGetAll_valid() throws ApiException {
        List<ProductPojo> productPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ProductPojo productPojo = new ProductPojo("barcode " + i, "name " + i, i + 1000.0, brandPojo.getId());
            productPojoList.add(productPojo);
            productService.add(productPojo);
        }

        List<ProductData> productDataList = productDto.getAll();
        for (int i = 0; i < 5; i++) {
            checkPojoAndData(productPojoList.get(i), productDataList.get(i));
        }
    }

    @Test
    public void testGetAll_null() throws ApiException {
        Assert.assertEquals(0, productDto.getAll().size());
    }

    @Test
    public void testUpdate_valid() throws ApiException {
        ProductPojo productPojo = new ProductPojo("existing_laptop", "thinkpad", 25000, brandPojo.getId());
        productService.add(productPojo);

        ProductForm productForm = new ProductForm("new_laptop", brandPojo.getId(), "thinkpad", 25000);
        productDto.update(productPojo.getId(), productForm);
        checkPojoAndForm(productForm, productService.get(productPojo.getId()));
    }

    @Test(expected = ApiException.class)
    public void testUpdate_Invalid() throws ApiException {
        ProductPojo productPojo1 = new ProductPojo("existing_laptop", "thinkpad", 25000, brandPojo.getId());
        productService.add(productPojo1);
        ProductPojo productPojo = new ProductPojo("laptop", "thinkpad", 25000, brandPojo.getId());
        productService.add(productPojo);

        ProductForm productForm = new ProductForm("existing_laptop", brandPojo.getId(), "thinkpad", 25000);
        productDto.update(productPojo.getId(), productForm);
    }

    @Test(expected = ApiException.class)
    public void test_checkBrand() throws ApiException {
        productDto.getBrand(2);
    }

    @Test
    public void test_checkBrand_invalid() throws ApiException {
        productDto.getBrand(brandPojo.getId());
    }

    @Test
    public void test_getBrand_valid() throws ApiException {
        BrandPojo brandPojo1 = productDto.getBrand(brandPojo.getId());
        Assert.assertEquals(brandPojo.getBrandName(), brandPojo1.getBrandName());
        Assert.assertEquals(brandPojo.getBrandCategory(), brandPojo1.getBrandCategory());
    }

    @Test(expected = ApiException.class)
    public void test_getBrand_invalid() throws ApiException {
        BrandPojo brandPojo1 = productDto.getBrand(brandPojo.getId() + 10);
    }
}
