package com.increff.employee.controller;

import com.increff.employee.dto.MakeOrderDto;
import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderForm;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// for orderItem table
@Api
@RestController
public class OrderItemController {
    @Autowired
    MakeOrderDto orderDto;

    @RequestMapping(path="/api/order",method = RequestMethod.POST)
    @ApiOperation(value = "To Add Order")
    public void add(@RequestBody List<OrderForm> orderForms) throws ApiException {
        System.out.println(orderForms.size());
        orderDto.add(orderForms);
    }

    @RequestMapping(path="/api/order/{barcode}",method = RequestMethod.GET)
    @ApiOperation("Gets the order")
    public OrderData get(@PathVariable String barcode) throws ApiException{

        return orderDto.get(barcode);
    }

    @ApiOperation(value = "Gets list of all orders")
    @RequestMapping(path = "/api/order", method = RequestMethod.GET)
    public List<OrderData> getAll() throws ApiException {
        return orderDto.getAll();
    }

}
