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
    MakeOrderDto orderDto;
    private boolean ordered=false;
    @RequestMapping(path="/api/order",method = RequestMethod.POST)
    @ApiOperation(value = "To Add Order")
    public void add(@RequestBody List<OrderForm> orderForms) throws ApiException {
        orderDto.add(orderForms);
        ordered=true;
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
        /*System.out.println("hi");
        try {
            DefaultResourceLoader loader = new DefaultResourceLoader();//path relative from resources
            InputStream is = loader.getResource("classpath:com/increff/employee/pdfs/order.pdf").getInputStream();
            IOUtils.copy(is, response.getOutputStream());
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=invoice.pdf");
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream"+ex);
        }
        */

        String pdfFileName = "invoice.pdf";
        //String contextPath = getServletContext().getRealPath(File.separator);
        String fileName="temp.pdf";
        if(ordered)
        fileName="src/main/resources/com/increff/employee/pdfs/order.pdf";

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
        ordered=false;
        responseOutputStream.flush();

    }
}
