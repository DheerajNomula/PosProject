package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.model.SalesForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.SalesReportResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemDao orderItemDao;

    protected static void checkOrderQuantity(int quantity) throws ApiException {
        if (quantity <= 0)
            throw new ApiException("Order Quantity cannot be negative");
    }

    protected static void checkSellingPrice(double price) throws ApiException {
        if (price <= 0)
            throw new ApiException("Selling Price cannot be negative");
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(OrderItemPojo orderItemPojo) throws ApiException {
        checkOrderQuantity(orderItemPojo.getQuantity());
        checkSellingPrice(orderItemPojo.getSellingPrice());
        orderItemDao.insert(orderItemPojo);
    }

    @Transactional(readOnly = true)
    public List<OrderItemPojo> getByOrderId(int id) {
        return orderItemDao.getByOrderId(id);
    }

    protected Date subtractOneYear(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR,-1);
        return calendar.getTime();
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<SalesReportResult> salesReport(SalesForm salesForm) throws ApiException {
        if (salesForm.getEndDate() == null)
            salesForm.setEndDate(new Date());
        if (salesForm.getStartDate() == null)
            salesForm.setStartDate(new Date(1));// make it as 1yr before
        if (salesForm.getEndDate().compareTo(salesForm.getStartDate()) < 0) {
            throw new ApiException("Enter valid start and end dates dates");
        }
        checkAndSubtractDate(salesForm);
        return orderItemDao.salesReport(salesForm);

    }

    private void checkAndSubtractDate(SalesForm salesForm) {
        Date dt1=salesForm.getStartDate();
        Date dt2=salesForm.getEndDate();
        int diffInDays = (int) ((dt2.getTime() - dt1.getTime()) / (1000 * 60 * 60 * 24));
        if(diffInDays>365){
            salesForm.setStartDate(subtractOneYear(salesForm.getEndDate()));
        }
    }
}
