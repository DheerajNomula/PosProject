package com.increff.employee.controller;
import com.increff.employee.pojo.BrandPojo;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import com.increff.employee.dto.ReportsDto;
import com.increff.employee.model.*;
import com.increff.employee.service.ApiException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


import javax.servlet.http.HttpServletResponse;

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
    public List<InventoryData> generateInventory() {
        return reportsDto.getInventoryData();
    }

    @ApiOperation(value = "Generates the brand Report")
    @RequestMapping(path = "/api/reports/brand", method = RequestMethod.GET)
    public List<BrandPojo> generateBrand()  {
        return reportsDto.getBrand();
    }

}
