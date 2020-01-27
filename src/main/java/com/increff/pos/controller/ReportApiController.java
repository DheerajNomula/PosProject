package com.increff.pos.controller;

import com.increff.pos.dto.ReportsDto;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.SalesData;
import com.increff.pos.model.SalesForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class ReportApiController {

    @Autowired
    private ReportsDto reportsDto;

    @ApiOperation(value = "Generates the sales Report")
    @RequestMapping(path = "/api/reports/sales", method = RequestMethod.POST)
    public List<SalesData> generateSales(@RequestBody SalesForm salesForm) throws ApiException {
        return reportsDto.getSalesData(salesForm);
    }

    @ApiOperation(value = "Generates the inventory Report")
    @RequestMapping(path = "/api/reports/inventory", method = RequestMethod.GET)
    public List<InventoryData> generateInventory(){
        return reportsDto.getInventoryData();
    }

    @ApiOperation(value = "Generates the brand Report")
    @RequestMapping(path = "/api/reports/brand", method = RequestMethod.GET)
    public List<BrandPojo> generateBrand() {
        return reportsDto.getBrand();
    }

}
