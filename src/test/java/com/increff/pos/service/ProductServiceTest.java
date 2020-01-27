package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductDisplayResult;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductServiceTest extends AbstractUnitTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductDao productDao;

    @Test
    public void testNormalize() throws ApiException {
        ProductPojo productPojo = new ProductPojo(" LeNovo123 ", "thiNkPad", 25000.00, 1);
        productService.normalize(productPojo);
        Assert.assertEquals("lenovo123", productPojo.getBarcode());
        Assert.assertEquals("thinkpad", productPojo.getProductName());
    }

    @Test
    public void testNormalize_null() throws ApiException {
        ProductPojo productPojo = new ProductPojo(" ", "thiNkPad", 25000.00, 1);
        productService.normalize(productPojo);
        Assert.assertEquals("", productPojo.getBarcode());
        Assert.assertEquals("thinkpad", productPojo.getProductName());
    }

    @Test
    public void testAdd() throws ApiException { //valid add
        ProductPojo productPojo = new ProductPojo(" LeNovo123 ", "thiNkPad", 25000.00, 1);
        productService.add(productPojo);

        List<ProductDisplayResult> productPojoList = productDao.selectAll();
        Assert.assertEquals((productPojoList.get(0)).getId(), productPojo.getId());
    }

    @Test(expected = ApiException.class)
    public void testCheck_null_barcode() throws ApiException { //invalid(barcode) add
        ProductPojo productPojo = new ProductPojo("   ", "thiNkPad", 25000.00, 1);
        productService.check(productPojo);
    }

    @Test(expected = ApiException.class) // invalid barcode
    public void testAdd_invalid_DuplicateBarcode() throws ApiException {
        ProductPojo productPojo1 = new ProductPojo("lenovo123", "laptops", 15000, 1);
        productService.add(productPojo1);
        ProductPojo productPojo2 = new ProductPojo("lenovo123", "xyz", 16000, 1);
        productService.add(productPojo2);
    }

    @Test(expected = ApiException.class)
    public void testCheck_null_name() throws ApiException { //invalid (name) add
        ProductPojo productPojo = new ProductPojo(" LeNovo123 ", " ", 25000.00, 1);
        productService.check(productPojo);
    }

    // no test on invalid brand since it has to be checked in dto

    @Test(expected = ApiException.class)
    public void testAdd_invalid_mrp() throws ApiException {//invalid (mrp) add
        ProductPojo productPojo = new ProductPojo(" LeNovo123 ", " thiNKPad", -25000.00, 1);
        productService.add(productPojo);
    }

    @Test
    public void testGet_valid() throws ApiException {
        ProductPojo productPojo = new ProductPojo("lenovo123", "thinkpad", 25000.00, 1);
        productDao.insert(productPojo);

        ProductPojo fromDb = productService.get(productPojo.getId());
        Assert.assertEquals(productPojo.getBarcode(), fromDb.getBarcode());
    }

    @Test(expected = ApiException.class)
    public void testGet_invalid() throws ApiException {
        ProductPojo productPojo = new ProductPojo("lenovo123", "thinkpad", 25000.00, 1);
        productDao.insert(productPojo);

        ProductPojo fromDb = productService.get(productPojo.getId() + 10);
        Assert.assertEquals(productPojo.getBarcode(), fromDb.getBarcode());
    }

    @Test
    public void testGetAll_valid() throws ApiException {
        for (int i = 0; i < 5; i++) {
            ProductPojo productPojo = new ProductPojo(" barcode " + i, "name" + i, 25000.00, 1);
            productDao.insert(productPojo);
        }

        List<ProductDisplayResult> productPojoList = productService.getAll();
        Assert.assertEquals(5, productPojoList.size());
    }

    @Test
    public void testGetAll_null() throws ApiException {
        List<ProductDisplayResult> productPojoList = productService.getAll();
        Assert.assertEquals(0, productPojoList.size());
    }

    @Test
    public void testGetCheck_valid() throws ApiException {
        ProductPojo productPojo = new ProductPojo("lenovo123", "thinkpad", 25000.00, 1);
        productDao.insert(productPojo);

        ProductPojo fromDb = productService.getCheck(productPojo.getId());
        Assert.assertEquals(productPojo.getBarcode(), fromDb.getBarcode());
        Assert.assertEquals(productPojo.getProductName(), fromDb.getProductName());
    }

    @Test(expected = ApiException.class)
    public void testGetCheck_invalid() throws ApiException {
        ProductPojo productPojo = new ProductPojo("lenovo123", "thinkpad", 25000.00, 1);
        productDao.insert(productPojo);

        ProductPojo fromDb = productService.getCheck(productPojo.getId() + 1);
    }

    @Test
    public void testUpdate_valid() throws ApiException {
        ProductPojo productPojo = new ProductPojo("lenovo123", "thinkpad", 25000.00, 1);
        productDao.insert(productPojo);

        ProductPojo productPojo1 = new ProductPojo("lenovo12", "laptops", 15000, 1);
        productService.update(productPojo.getId(), productPojo1);
        productPojo = productDao.select(productPojo.getId());
        Assert.assertEquals(productPojo1.getBarcode(), productPojo.getBarcode());
        Assert.assertEquals(productPojo1.getProductName(), productPojo.getProductName());
        Assert.assertEquals(productPojo1.getMrp(), productPojo.getMrp(), 0.00f);
    }

    @Test(expected = ApiException.class)
    public void testUpdate_Invalid_Id() throws ApiException {
        ProductPojo productPojo = new ProductPojo("lenovo123", "thinkpad", 25000.00, 1);
        productDao.insert(productPojo);

        productService.update(productPojo.getId() + 1, new ProductPojo(" LeNovo123 ", " thiNKPad", 25000.00, 1));
    }

    @Test(expected = ApiException.class)
    public void testUpdate_Invalid_DuplicateBarcode() throws ApiException {
        ProductPojo productPojo = new ProductPojo("lenovo123", "thinkPad", 25000.00, 1);
        productDao.insert(productPojo);
        ProductPojo productPojo1 = new ProductPojo("lenovo12", "thinkspad", 15000, 1);
        productDao.insert(productPojo1);

        productService.update(productPojo1.getId(), new ProductPojo("LeNovo123 ", " thiNKPad", 25000.00, 1));
    }

    @Test(expected = ApiException.class)
    public void testUpdate_Invalid_barcode() throws ApiException {
        ProductPojo productPojo = new ProductPojo("lenovo123", "thinkPad", 25000.00, 1);
        productDao.insert(productPojo);
        ProductPojo productPojo1 = new ProductPojo(" ", "thinkpad", 15000, 1);

        productService.update(productPojo.getId(), productPojo1);
    }
    //remainging cases already checked in add

    @Test
    public void testGetProductByBarcode_valid() throws ApiException {
        ProductPojo productPojo = new ProductPojo("lenovo123", "thinkpad", 25000.00, 1);
        productDao.insert(productPojo);

        ProductPojo fromDb = productService.getProductByBarcode(productPojo.getBarcode());
        Assert.assertEquals(productPojo.getProductName(), fromDb.getProductName());
        Assert.assertEquals(productPojo.getMrp(), fromDb.getMrp(), 0.00f);
        Assert.assertEquals(productPojo.getBrandId(), fromDb.getBrandId());

    }

    @Test
    public void testGetProductByBarcode_invalid() throws ApiException {
        ProductPojo productPojo = new ProductPojo("lenovo123", "thinkpad", 25000.00, 1);
        productDao.insert(productPojo);

        ProductPojo fromDb = productService.getProductByBarcode("invalidBarcode");
        Assert.assertEquals(null, fromDb);
    }

    @Test
    public void testCheckTd_valid() throws ApiException {
        ProductPojo productPojo = new ProductPojo("lenovo123", "thinkpad", 25000.00, 1);
        productDao.insert(productPojo);

        int number = productService.checkId(productPojo.getId());
        Assert.assertEquals(1, number);
    }

    @Test
    public void testCheckTd_invalid() throws ApiException {
        ProductPojo productPojo = new ProductPojo("lenovo123", "thinkpad", 25000.00, 1);
        productDao.insert(productPojo);

        int number = productService.checkId(productPojo.getId() + 1);
        Assert.assertEquals(0, number);
    }


}
