package com.increff.employee.dto;

import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.ProductService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class InventoryDtoTest extends AbstractUnitTest {
    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;
    private BrandPojo brandPojo;
    private ProductPojo productPojo;

    @Before
    public void init() throws ApiException {
        brandPojo=new BrandPojo("lenovo","laptops");
        brandService.add(brandPojo);

        productPojo=new ProductPojo("laptop123","thinkpad",15000,brandPojo.getId());
        productService.add(productPojo);
    }
    @Test
    public void testCheckInventory_valid() throws ApiException {

        InventoryPojo inventoryPojo=new InventoryPojo(productPojo.getId(),20);
        Assert.assertEquals(false,inventoryDto.checkProductId(inventoryPojo));
    }

    @Test(expected = ApiException.class)
    public void testCheckProductId_inValid() throws ApiException {
        InventoryPojo inventoryPojo=new InventoryPojo(productPojo.getId()+10,20);
        Assert.assertEquals(false,inventoryDto.checkProductId(inventoryPojo));
    }

    @Test
    public void testAdd_Valid() throws ApiException {
        InventoryForm inventoryForm=new InventoryForm("laptop123",20);
        InventoryPojo inventoryPojo=inventoryDto.add(inventoryForm);
        Assert.assertEquals(20,inventoryPojo.getQuantity());
    }

    @Test
    public void testAdd_Valid_repeated() throws ApiException {
        InventoryForm inventoryForm=new InventoryForm("laptop123",20);
        InventoryPojo inventoryPojo=inventoryDto.add(inventoryForm);
        inventoryDto.add(inventoryForm);
        Assert.assertEquals(40,(inventoryDto.get(inventoryPojo.getId())).getQuantity());
    }

    //if qty<0 then making qty=0
    @Test
    public void testAdd_Valid_repeatedNegative() throws ApiException {
        InventoryForm inventoryForm=new InventoryForm("laptop123",-20);
        InventoryPojo inventoryPojo=inventoryDto.add(inventoryForm);
        inventoryDto.add(inventoryForm);
        Assert.assertEquals(0,(inventoryDto.get(inventoryPojo.getId())).getQuantity());
    }

    @Test
    public void testAdd_inValid_negativeQuantity() throws ApiException {
        InventoryForm inventoryForm=new InventoryForm("laptop123",-20);
        InventoryPojo inventoryPojo=inventoryDto.add(inventoryForm);
        Assert.assertEquals(0,(inventoryDto.get(inventoryPojo.getId())).getQuantity());
    }

}
