package com.increff.pos.dto;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryDisplayResult;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InventoryDtoTest extends AbstractUnitTest {
    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;
    @Autowired
    private InventoryService inventoryService;
    private BrandPojo brandPojo;
    private ProductPojo productPojo;

    @Before
    public void init() throws ApiException {
        brandPojo = new BrandPojo("lenovo", "laptops");
        brandService.add(brandPojo);

        productPojo = new ProductPojo("laptop123", "thinkpad", 15000, brandPojo.getId());
        productService.add(productPojo);
    }

//    @Test
//    public void testCheckProductId_valid() throws ApiException {
//        InventoryPojo inventoryPojo = new InventoryPojo(productPojo.getId(), 20);
//        Assert.assertEquals(false, inventoryDto.checkProductId(inventoryPojo));
//    }
//
//    @Test(expected = ApiException.class)
//    public void testCheckProductId_inValid() throws ApiException {
//        InventoryPojo inventoryPojo = new InventoryPojo(productPojo.getId() + 10, 20);
//        Assert.assertEquals(false, inventoryDto.checkProductId(inventoryPojo));
//    }
//
//    @Test //if product exists it adds qty to the existing qty
//    public void testCheckProductId_Valid_increment() throws ApiException {
//        InventoryPojo inventoryPojo = new InventoryPojo(productPojo.getId(), 20);
//        inventoryService.add(inventoryPojo);
//        Assert.assertEquals(true, inventoryDto.checkProductId(inventoryPojo));
//        Assert.assertEquals(40, inventoryService.get(inventoryPojo.getId()).getQuantity());
//    }

    @Test
    public void testAdd_Valid() throws ApiException {
        InventoryForm inventoryForm = new InventoryForm("laptop123", 20);
        inventoryDto.add(inventoryForm);
        List<InventoryDisplayResult> inventoryPojos=inventoryService.getAll();
        Assert.assertEquals(20, inventoryPojos.get(0).getQuantity());
    }

    @Test
    public void testAdd_Valid_repeated() throws ApiException {
        InventoryForm inventoryForm = new InventoryForm("laptop123", 20);
        inventoryDto.add(inventoryForm);
        inventoryDto.add(inventoryForm);
        List<InventoryDisplayResult> inventoryPojos=inventoryService.getAll();
        Assert.assertEquals(40, inventoryPojos.get(0).getQuantity());
    }

    //if qty<0 then making qty=0
    @Test //inserting -ve 2times
    public void testAdd_inValid_repeatedNegative() throws ApiException {
        InventoryForm inventoryForm = new InventoryForm("laptop123", -20);
        inventoryDto.add(inventoryForm);
        inventoryDto.add(inventoryForm);
        List<InventoryDisplayResult> inventoryPojos=inventoryService.getAll();
        Assert.assertEquals(0, inventoryPojos.get(0).getQuantity());
    }

    @Test  //inserting -ve
    public void testAdd_inValid_negativeQuantity() throws ApiException {
        InventoryForm inventoryForm = new InventoryForm("laptop123", -20);
        inventoryDto.add(inventoryForm);
        List<InventoryDisplayResult> inventoryPojos=inventoryService.getAll();
        Assert.assertEquals(0, inventoryPojos.get(0).getQuantity());
    }

    @Test
    public void testGet_valid() throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo(productPojo.getId(), 20);
        inventoryService.add(inventoryPojo);
        InventoryData inventoryData = inventoryDto.get(inventoryPojo.getId());
        Assert.assertEquals(inventoryData.getProductName(), "thinkpad");
        Assert.assertEquals(inventoryData.getBrandCategory(), "laptops");
        Assert.assertEquals(inventoryData.getBrandName(), "lenovo");
        Assert.assertEquals(inventoryData.getId(), inventoryPojo.getId());
    }

    @Test(expected = ApiException.class)
    public void testGet_invalid() throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo(productPojo.getId(), 20);
        inventoryService.add(inventoryPojo);
        InventoryData inventoryData = inventoryDto.get(inventoryPojo.getId() + 10);
    }

    @Test
    public void testGetAll_valid() throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo(productPojo.getId(), 20);
        inventoryService.add(inventoryPojo);

        List<InventoryData> list = inventoryDto.getAll();
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void testUpdate_valid() throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo(productPojo.getId(), 20);
        inventoryService.add(inventoryPojo);

        InventoryForm inventoryForm = new InventoryForm("laptop123", 22);
        inventoryDto.update(inventoryPojo.getId(), inventoryForm);
        Assert.assertEquals(22, inventoryService.get(inventoryPojo.getId()).getQuantity());
    }

    @Test
    public void testUpdate_invalid_quantity() throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo(productPojo.getId(), 20);
        inventoryService.add(inventoryPojo);

        InventoryForm inventoryForm = new InventoryForm("laptop123", -22);
        inventoryDto.update(inventoryPojo.getId(), inventoryForm);
        Assert.assertEquals(0, inventoryService.get(inventoryPojo.getId()).getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testUpdate_invalid_id() throws ApiException {
        InventoryForm inventoryForm = new InventoryForm("laptop123", -22);

        inventoryDto.update(20, inventoryForm);
    }

    @Test
    public void test_convertPojoToData() throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo(productPojo.getId(), 20);
        InventoryData inventoryData = inventoryDto.convert(inventoryPojo);
        Assert.assertEquals(inventoryPojo.getId(), inventoryData.getId());
        Assert.assertEquals("lenovo", inventoryData.getBrandName());
        Assert.assertEquals("laptops", inventoryData.getBrandCategory());
        Assert.assertEquals("thinkpad", inventoryData.getProductName());
    }

    @Test(expected = ApiException.class)
    public void test_convertPojoToData_invalid() throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo(productPojo.getId() + 10, 20);
        InventoryData inventoryData = inventoryDto.convert(inventoryPojo);
    }

    @Test
    public void test_getBrand_valid() throws ApiException {
        BrandPojo brandPojo1 = inventoryDto.getBrand(brandPojo.getId());
        Assert.assertEquals("lenovo", brandPojo1.getBrandName());
        Assert.assertEquals("laptops", brandPojo1.getBrandCategory());
        Assert.assertEquals(brandPojo.getId(), brandPojo1.getId());
    }

    @Test(expected = ApiException.class)
    public void test_getBrand_invalid() throws ApiException {
        BrandPojo brandPojo1 = inventoryDto.getBrand(brandPojo.getId() + 10);
    }

    @Test
    public void test_convertFormToPojo_valid() throws ApiException {
        InventoryForm inventoryForm = new InventoryForm("laptop123", 20);
        InventoryPojo inventoryPojo = inventoryDto.convert(inventoryForm);
        Assert.assertEquals(inventoryPojo.getQuantity(), inventoryForm.getQuantity());
        Assert.assertEquals(inventoryPojo.getId(), productPojo.getId());
    }

    @Test(expected = ApiException.class)
    public void test_convertFormToPojo_invalid() throws ApiException {
        InventoryForm inventoryForm = new InventoryForm("invalid", 20);
        InventoryPojo inventoryPojo = inventoryDto.convert(inventoryForm);
    }

    @Test
    public void test_getProduct_valid() throws ApiException {
        ProductPojo productPojo1 = inventoryDto.getProduct(productPojo.getId());
        Assert.assertEquals(productPojo1.getId(), productPojo.getId());
        Assert.assertEquals(productPojo1.getBrandId(), productPojo.getBrandId());
        Assert.assertEquals(productPojo1.getMrp(), productPojo.getMrp(), 0.0f);
        Assert.assertEquals(productPojo1.getProductName(), productPojo.getProductName());
        Assert.assertEquals(productPojo1.getBarcode(), productPojo.getBarcode());
    }

    @Test(expected = ApiException.class)
    public void test_getProduct_invalid() throws ApiException {
        ProductPojo productPojo1 = inventoryDto.getProduct(productPojo.getId() + 10);
    }

    @Test //no exception so test is passed
    public void test_getProductId_valid() throws ApiException {
        inventoryDto.getProductId("laptop123");
    }

    @Test(expected = ApiException.class)
    public void test_getProductId_invalid() throws ApiException {
        inventoryDto.getProductId("invalid");
    }

    @Test
    public void test_checkProductId_valid() throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo(productPojo.getId(), 20);
        inventoryService.add(inventoryPojo);
        inventoryDto.checkProductId(inventoryPojo);
        Assert.assertEquals(40, inventoryPojo.getQuantity());
    }

    @Test(expected = ApiException.class)
    public void test_checkProductId_invalid_product() throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo(productPojo.getId() + 10, 20);
        inventoryDto.checkProductId(inventoryPojo);
    }

//    @Test
//    public void test_checkProductId_valid_notinInventory() throws ApiException {
//        InventoryPojo inventoryPojo = new InventoryPojo(productPojo.getId(), 20);
//        Assert.assertEquals(false, inventoryDto.checkProductId(inventoryPojo));
//        Assert.assertEquals(20, inventoryPojo.getQuantity());
//    }
}
