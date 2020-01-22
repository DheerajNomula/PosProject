package com.increff.employee.service;

import com.increff.employee.model.InventoryData;
import com.increff.employee.pojo.InventoryPojo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InventoryServiceTest extends AbstractUnitTest {

    @Autowired
    private InventoryService inventoryService;
    @Test
    public void testCheckQuantity_valid() throws ApiException {
        InventoryPojo inventoryPojo=new InventoryPojo(1,20);
        inventoryService.checkQuantity(inventoryPojo);
        Assert.assertEquals(20,inventoryPojo.getQuantity());
    }

    @Test
    public void testCheckQuantity_invalid() throws ApiException {
        InventoryPojo inventoryPojo=new InventoryPojo(1,-20);
        inventoryService.checkQuantity(inventoryPojo);
        Assert.assertEquals(0,inventoryPojo.getQuantity());
    }
    //productid is existing or not is checked in dto layer
    @Test
    public void testAdd_valid() throws ApiException {
        InventoryPojo inventoryPojo=new InventoryPojo(1,20);
        inventoryService.add(inventoryPojo);
        Assert.assertEquals(1,inventoryPojo.getId());
    }

    @Test
    public void testAdd_invalid_quantity() throws ApiException {
        InventoryPojo inventoryPojo=new InventoryPojo(1,-20);
        inventoryService.add(inventoryPojo);
        Assert.assertEquals(1,inventoryPojo.getId());
        Assert.assertEquals(0,inventoryPojo.getQuantity());
    }

    //duplicate productid in inventory is checked in dto layer
    @Test
    public void testGet_valid() throws ApiException {
        InventoryPojo inventoryPojo=new InventoryPojo(1,20);
        inventoryService.add(inventoryPojo);
        InventoryPojo fromDb=inventoryService.get(inventoryPojo.getId());
        Assert.assertEquals(inventoryPojo.getQuantity(),fromDb.getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testGet_invalid() throws ApiException {
        InventoryPojo inventoryPojo=new InventoryPojo(1,20);
        inventoryService.add(inventoryPojo);
        InventoryPojo fromDb=inventoryService.get(inventoryPojo.getId()+1);
    }

    @Test
    public void testGetAll() throws ApiException {
        for(int i=0;i<5;i++){
            InventoryPojo inventoryPojo=new InventoryPojo(i,20);
            inventoryService.add(inventoryPojo);
        }

        List<InventoryPojo> list=inventoryService.getAll();
        Assert.assertEquals(5,list.size());
    }

    @Test
    public void testGetAll_null() throws ApiException {
        List<InventoryPojo> list=inventoryService.getAll();
        Assert.assertEquals(0,list.size());
    }

    @Test
    public void testUpdate_valid() throws ApiException {
        InventoryPojo inventoryPojo=new InventoryPojo(1,20);
        inventoryService.add(inventoryPojo);

        InventoryPojo inventoryPojo1=new InventoryPojo(1,50);
        inventoryService.update(1,inventoryPojo1);
        Assert.assertEquals(50,(inventoryService.get(1)).getQuantity());
    }

    @Test
    public void testUpdate_Invalid_negativeQuantity() throws ApiException {
        InventoryPojo inventoryPojo=new InventoryPojo(1,20);
        inventoryService.add(inventoryPojo);

        InventoryPojo inventoryPojo1=new InventoryPojo(1,-50);
        inventoryService.update(1,inventoryPojo1);
        Assert.assertEquals(0,(inventoryService.get(1)).getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testUpdate_Invalid_NoId() throws ApiException {
        InventoryPojo inventoryPojo1=new InventoryPojo(1,-50);
        inventoryService.update(1,inventoryPojo1);
        Assert.assertEquals(0,(inventoryService.get(1)).getQuantity());
    }

    @Test
    public void testgetCheck_valid() throws ApiException {
        InventoryPojo inventoryPojo=new InventoryPojo(1,20);
        inventoryService.add(inventoryPojo);

        inventoryService.getCheck(1);
        Assert.assertEquals(20,(inventoryService.get(1)).getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testgetCheck_invalid() throws ApiException {
        inventoryService.getCheck(1);
    }
}
