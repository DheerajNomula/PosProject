package com.increff.employee.service;
import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.model.SalesForm;
import com.increff.employee.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemDao orderItemDao;

    @Transactional(rollbackFor = ApiException.class)
    public void add(OrderItemPojo orderItemPojo) throws ApiException {
        checkOrderQuantity(orderItemPojo.getQuantity());
        checkSellingPrice(orderItemPojo.getSellingPrice());
        orderItemDao.insert(orderItemPojo);
    }

    protected static void checkOrderQuantity(int quantity) throws ApiException {
        if(quantity<=0)
            throw new ApiException("Order Quantity cannot be negative");
    }

    protected static void checkSellingPrice(double price) throws ApiException {
        if(price<=0)
            throw new ApiException("Selling Price cannot be negative");
    }

    @Transactional(readOnly = true)
    public List<OrderItemPojo> getByOrderId(int id) {
        return orderItemDao.getByOrderId(id);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<Object[]> salesReport(SalesForm salesForm) throws ApiException {
        if(salesForm.getEndDate()==null)
            salesForm.setEndDate(new Date());
        if(salesForm.getStartDate()==null)
            salesForm.setStartDate(new Date(1));
        if(salesForm.getEndDate().compareTo(salesForm.getStartDate())<0){
            throw new ApiException("Enter valid start and end dates dates");
        }

        return orderItemDao.salesReport(salesForm);

    }
}
