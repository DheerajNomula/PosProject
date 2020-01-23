package com.increff.employee.service;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderPojo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class OrderServiceTest extends AbstractUnitTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Test(expected = ApiException.class)
    public void testCheckNullDate_invalid() throws ApiException {
        Date date=null;
        orderService.checkNullDate(date);
    }

    @Test
    public void testCheckNullDate_valid() throws ApiException {
        Date date=new Date();
        orderService.checkNullDate(date);
    }

    @Test
    public void testAdd_valid() throws ApiException {
        OrderPojo orderPojo=new OrderPojo(new Date());
        orderService.add(orderPojo);
        List<OrderPojo> orderPojoList=orderDao.selectAll();
        Assert.assertEquals((orderPojoList.get(0)).getId(),orderPojo.getId());
        Assert.assertEquals((orderPojoList.get(0)).getDate(),orderPojo.getDate());
    }

    @Test(expected = ApiException.class)
    public void testAdd_invalid() throws ApiException { //no date
        OrderPojo orderPojo=new OrderPojo();
        orderService.add(orderPojo);
    }

    @Test
    public void testGet_valid() throws ApiException {
        OrderPojo orderPojo=new OrderPojo(new Date());
        orderService.add(orderPojo);
        Assert.assertEquals(orderPojo.getId(),(orderService.get(orderPojo.getId())).getId());
    }
    @Test(expected =ApiException.class)
    public void testGet_invalid() throws ApiException {
        Assert.assertEquals(1,(orderService.get(1)).getId());
    }

    @Test
    public void testGetAll_valid() throws ApiException {
        for(int i=0;i<5;i++) {
            OrderPojo orderPojo = new OrderPojo(new Date());
            orderDao.insert(orderPojo);
        }

        List<OrderPojo> orderPojos=orderService.getAll();
        Assert.assertEquals(5,orderPojos.size());
    }

    @Test
    public void testGetAll_null() throws ApiException {
        List<OrderPojo> orderPojos=orderService.getAll();
        Assert.assertEquals(0,orderPojos.size());
    }

    @Test
    public void testUpdate_valid() throws ApiException {
        OrderPojo orderPojo=new OrderPojo(new Date());
        orderDao.insert(orderPojo);
        OrderPojo orderPojo1=new OrderPojo(new Date());
        orderService.update(orderPojo.getId(),orderPojo1);
        Assert.assertEquals(orderPojo1.getDate(),(orderDao.select(orderPojo.getId())).getDate());
    }

    @Test(expected = ApiException.class)
    public void testUpdate_invalid() throws ApiException {
        OrderPojo orderPojo1=new OrderPojo(new Date());
        orderService.update(1,orderPojo1);
    }

    @Test
    public void testgetCheck_valid() throws ApiException {
        OrderPojo orderPojo=new OrderPojo(new Date());
        orderDao.insert(orderPojo);
        Assert.assertEquals(orderPojo.getDate(),(orderService.getCheck(orderPojo.getId())).getDate());
    }

    @Test(expected = ApiException.class)
    public void testgetCheck_invalid() throws ApiException {
        orderService.getCheck(1);
    }


}
