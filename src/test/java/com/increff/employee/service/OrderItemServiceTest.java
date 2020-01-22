package com.increff.employee.service;

import com.increff.employee.pojo.OrderItemPojo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderItemServiceTest extends AbstractUnitTest {
    @Autowired
    private OrderItemService orderItemService;

    @Test  //valid so should not throw any error
    public void testCheckOrderQuantity_valid() throws ApiException {
        orderItemService.checkOrderQuantity(12);
    }

    @Test(expected = ApiException.class)
    public void testCheckOrderQuantity_invalid() throws ApiException {
        orderItemService.checkOrderQuantity(-12);
    }

    @Test(expected = ApiException.class) //cannot be zero
    public void testCheckOrderQuantity_zero() throws ApiException {
        orderItemService.checkOrderQuantity(0);
    }

    @Test  //valid so should not throw any error
    public void testCheckSellingPrice_valid() throws ApiException {
        orderItemService.checkSellingPrice(12);
    }

    @Test(expected = ApiException.class)
    public void testCheckSellingPrice_invalid() throws ApiException {
        orderItemService.checkSellingPrice(-12);
    }

    @Test(expected = ApiException.class)
    public void testCheckSellingPrice_zero() throws ApiException {
        orderItemService.checkSellingPrice(0);
    }

    //getcheck is not needed but included only for testing purpose
    @Test
    public void testAdd_valid() throws ApiException {
        OrderItemPojo orderItemPojo=new OrderItemPojo(1,1,20,200);
        orderItemService.add(orderItemPojo);
        OrderItemPojo fromDb=orderItemService.getCheck(orderItemPojo.getId());
        Assert.assertEquals(orderItemPojo.getId(),fromDb.getId());
        Assert.assertEquals(orderItemPojo.getQuantity(),fromDb.getQuantity());
        Assert.assertEquals(orderItemPojo.getSellingPrice(),fromDb.getSellingPrice(),0.00f);
        Assert.assertEquals(orderItemPojo.getOrderId(),fromDb.getOrderId());
    }

    @Test(expected = ApiException.class)
    public void testAdd_invalid_qty() throws ApiException {
        OrderItemPojo orderItemPojo=new OrderItemPojo(1,1,-20,200);
        orderItemService.add(orderItemPojo);
    }

    @Test(expected = ApiException.class)
    public void testAdd_invalid_sellingprice() throws ApiException {
        OrderItemPojo orderItemPojo=new OrderItemPojo(1,1,20,-200);
        orderItemService.add(orderItemPojo);
    }

    @Test
    public void test_getByOrderId_valid() throws ApiException {
        OrderItemPojo orderItemPojo=new OrderItemPojo(1,1,20,200);
        orderItemService.add(orderItemPojo);

        OrderItemPojo orderItemPojo1=orderItemService.getByOrderId(1).get(0);
        Assert.assertEquals(orderItemPojo.getSellingPrice(),orderItemPojo1.getSellingPrice(),0.0f);
        Assert.assertEquals(orderItemPojo.getQuantity(),orderItemPojo1.getQuantity());
        Assert.assertEquals(orderItemPojo.getId(),orderItemPojo1.getId());
    }

    @Test
    public void test_getByOrderId_invalid() throws ApiException {
        OrderItemPojo orderItemPojo=new OrderItemPojo(1,1,20,200);
        orderItemService.add(orderItemPojo);
        Assert.assertEquals(0,orderItemService.getByOrderId(2).size());
    }

    //need to test salesreport func

}
