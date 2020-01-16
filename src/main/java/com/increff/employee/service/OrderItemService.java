package com.increff.employee.service;
import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemDao orderItemDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(OrderItemPojo orderItemPojo) throws ApiException {
        checkOrderQuantity(orderItemPojo.getQuantity());
        checkSellingPrice(orderItemPojo.getSellingPrice());
        orderItemDao.insert(orderItemPojo);
    }

    private void checkOrderQuantity(int quantity) throws ApiException {
        if(quantity<0)
            throw new ApiException("Order Quantity cannot be negative");
    }

    private void checkSellingPrice(double price) throws ApiException {
        if(price<0)
            throw new ApiException("Selling Price cannot be negative");
    }

    @Transactional(rollbackOn = ApiException.class)
    public OrderItemPojo get(int id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<OrderItemPojo> getAll(){
        return orderItemDao.selectAll();
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int id, OrderItemPojo orderItemPojo) throws ApiException {
        System.out.println(orderItemPojo);
        OrderItemPojo newOrderitemPojo=getCheck(id);;
        checkOrderQuantity(orderItemPojo.getQuantity());
        checkSellingPrice(orderItemPojo.getSellingPrice());

        //newOrderitemPojo.setId(orderItemPojo.getId());
        newOrderitemPojo.setOrderId(orderItemPojo.getOrderId());
        newOrderitemPojo.setProductId(orderItemPojo.getProductId());
        newOrderitemPojo.setQuantity(orderItemPojo.getQuantity());
        newOrderitemPojo.setSellingPrice(orderItemPojo.getSellingPrice());
    }

    private OrderItemPojo getCheck(int id) throws ApiException {
        OrderItemPojo orderItemPojo=orderItemDao.select(id);
        if(orderItemPojo==null)
            throw new ApiException("OrderItem with id :"+id+" doesn't exist");
        return orderItemPojo;
    }

}
