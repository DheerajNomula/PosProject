package com.increff.employee.controller;

import com.increff.employee.dto.MakeOrderDto;
import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderForm;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

// for orderItem table
@Api
@RestController
public class OrderItemController {
    @Autowired
    private MakeOrderDto orderDto;
    @RequestMapping(path="/api/order",method = RequestMethod.POST)
    @ApiOperation(value = "To Add Order")
    public void add(@RequestBody List<OrderForm> orderForms) throws ApiException {
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

    @ApiOperation(value = "Generates the pdf")
    @RequestMapping(value="/api/order/invoice",method = RequestMethod.GET)
    public void getFile(HttpServletResponse response) throws IOException {

        String pdfFileName = "invoice.pdf";
        String fileName="src/main/resources/com/increff/employee/pdfs/order.pdf";

        File pdfFile=new File(fileName);

        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "attachment; filename=" + pdfFileName);
        response.setContentLength((int) pdfFile.length());

        FileInputStream fileInputStream = new FileInputStream(pdfFile);
        OutputStream responseOutputStream = response.getOutputStream();
        int bytes;
        while ((bytes = fileInputStream.read()) != -1) {
            responseOutputStream.write(bytes);
        }
        responseOutputStream.flush();

    }
}
