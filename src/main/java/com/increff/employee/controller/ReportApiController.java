package com.increff.employee.controller;

import java.util.List;

import com.increff.employee.dto.ReportsDto;
import com.increff.employee.model.*;
import com.increff.employee.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ReportApiController {

    @Autowired
    private ReportsDto service;

    @ApiOperation(value = "Generates the sales Report")
    @RequestMapping(path = "/api/reports/sales", method = RequestMethod.POST)
    public List<SalesData> generateSales(@RequestBody SalesForm salesForm){
        //System.out.println(salesForm);
        return service.getSales(salesForm);
    }

    @ApiOperation(value = "Generates the inventory Report")
    @RequestMapping(path = "/api/reports/inventory", method = RequestMethod.GET)
    public List<InventoryData> generateInventory() throws ApiException {
        return service.getInventory();
    }

    @ApiOperation(value = "Generates the brand Report")
    @RequestMapping(path = "/api/reports/brand", method = RequestMethod.GET)
    public List<BrandData> generateBrand() {
        return service.getBrand();
    }

}
