package com.increff.pos.service;

import com.increff.pos.dao.*;
import com.increff.pos.model.SalesForm;
import com.increff.pos.pojo.*;
import com.increff.pos.dao.*;
import com.increff.pos.pojo.*;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrderItemServiceTest extends AbstractUnitTest {
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private InventoryDao inventoryDao;

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
        OrderItemPojo orderItemPojo = new OrderItemPojo(1, 1, 20, 200);
        orderItemService.add(orderItemPojo);
        OrderItemPojo fromDb = orderItemDao.select(orderItemPojo.getId());
        Assert.assertEquals(orderItemPojo.getId(), fromDb.getId());
        Assert.assertEquals(orderItemPojo.getQuantity(), fromDb.getQuantity());
        Assert.assertEquals(orderItemPojo.getSellingPrice(), fromDb.getSellingPrice(), 0.00f);
        Assert.assertEquals(orderItemPojo.getOrderId(), fromDb.getOrderId());
    }

    @Test(expected = ApiException.class)
    public void testAdd_invalid_qty() throws ApiException {
        OrderItemPojo orderItemPojo = new OrderItemPojo(1, 1, -20, 200);
        orderItemService.add(orderItemPojo);
    }

    @Test(expected = ApiException.class)
    public void testAdd_invalid_sellingprice() throws ApiException {
        OrderItemPojo orderItemPojo = new OrderItemPojo(1, 1, 20, -200);
        orderItemService.add(orderItemPojo);
    }

    @Test
    public void test_getByOrderId_valid() throws ApiException {
        OrderItemPojo orderItemPojo = new OrderItemPojo(1, 1, 20, 200);
        orderItemDao.insert(orderItemPojo);

        OrderItemPojo orderItemPojo1 = orderItemService.getByOrderId(1).get(0);
        Assert.assertEquals(orderItemPojo.getSellingPrice(), orderItemPojo1.getSellingPrice(), 0.0f);
        Assert.assertEquals(orderItemPojo.getQuantity(), orderItemPojo1.getQuantity());
        Assert.assertEquals(orderItemPojo.getId(), orderItemPojo1.getId());
    }

    @Test
    public void test_getByOrderId_invalid() throws ApiException {
        OrderItemPojo orderItemPojo = new OrderItemPojo(1, 1, 20, 200);
        orderItemDao.insert(orderItemPojo);
        Assert.assertEquals(0, orderItemService.getByOrderId(2).size());
    }

    @Test
    public void testSalesReport_valid_null() throws ApiException {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 1);
        SalesForm salesForm = new SalesForm(new Date(), c.getTime(), "lenovo", "laptops");
        List<SalesReportResult> sales = orderItemService.salesReport(salesForm);
        Assert.assertEquals(0, sales.size());
    }

    @Test
    public void testSalesReport_valid() throws ApiException {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 1);
        SalesForm salesForm = new SalesForm(new Date(), c.getTime(), "lenovo", "laptops");
        BrandPojo brandPojo=new BrandPojo("lenovo","laptops");
        brandDao.insert(brandPojo);
        ProductPojo productPojo=new ProductPojo("laptop123","thinkpad",200,brandPojo.getId());
        productDao.insert(productPojo);
        InventoryPojo inventoryPojo=new InventoryPojo(productPojo.getId(),20);
        inventoryDao.insert(inventoryPojo);
        OrderPojo orderPojo = new OrderPojo(new Date());
        orderDao.insert(orderPojo);

        OrderItemPojo orderItemPojo = new OrderItemPojo(orderPojo.getId(), productPojo.getId(), 20, 200);
        orderItemDao.insert(orderItemPojo);


        List<SalesReportResult> sales = orderItemService.salesReport(salesForm);
        Assert.assertEquals(1, sales.size());
    }

    @Test(expected = ApiException.class)
    public void test_getByOrderId_start_end() throws ApiException {

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 1); // start>end
        SalesForm salesForm = new SalesForm(c.getTime(), new Date(), "lenovo", "laptops");

        OrderPojo orderPojo = new OrderPojo(new Date());
        orderDao.insert(orderPojo);

        OrderItemPojo orderItemPojo = new OrderItemPojo(orderPojo.getId(), 1, 20, 200);
        orderItemDao.insert(orderItemPojo);


        List<SalesReportResult> sales = orderItemService.salesReport(salesForm);
        Assert.assertEquals(1, sales.size());
    }

    @Test
    public void test_getByOrderId_null() throws ApiException {

        SalesForm salesForm = new SalesForm(null, null, "lenovo", "laptops");
        BrandPojo brandPojo=new BrandPojo("lenovo","laptops");
        brandDao.insert(brandPojo);

        ProductPojo productPojo=new ProductPojo("laptop123","thinkpad",200,brandPojo.getId());
        productDao.insert(productPojo);
        InventoryPojo inventoryPojo=new InventoryPojo(productPojo.getId(),20);
        inventoryDao.insert(inventoryPojo);

        OrderPojo orderPojo = new OrderPojo(new Date());
        orderDao.insert(orderPojo);

        OrderItemPojo orderItemPojo = new OrderItemPojo(orderPojo.getId(), productPojo.getId(), 20, 200);
        orderItemDao.insert(orderItemPojo);


        List<SalesReportResult> sales = orderItemService.salesReport(salesForm);
        Assert.assertEquals(1, sales.size());
    }

}
