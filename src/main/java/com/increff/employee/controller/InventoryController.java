package com.increff.employee.controller;

import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class InventoryController {
    @Autowired
    InventoryService inventoryService;

    @ApiOperation(value = "Adds the inventory")
    @RequestMapping(path = "/api/inventory",method = RequestMethod.POST)
    public void add(@RequestBody InventoryForm form) throws ApiException {
        InventoryPojo p=convert(form);
        inventoryService.add(p);
    }
    @ApiOperation(value = "Gets the inventory")
    @RequestMapping(path="/api/inventory/{id}",method = RequestMethod.GET)
    public InventoryData get(@PathVariable int id) throws ApiException {
        InventoryPojo p=inventoryService.get(id);
        return convert(p);
    }

    @ApiOperation(value = "Gets list of all inventory")
    @RequestMapping(path="/api/inventory",method = RequestMethod.GET)
    public List<InventoryData> getAll() throws ApiException {
        List<InventoryPojo> list=inventoryService.getAll();
        List<InventoryData> list2=new ArrayList<InventoryData>();
        for(InventoryPojo p:list){
            list2.add(convert(p));
        }
        return list2;
    }
    @ApiOperation(value="Update the inventory")
    @RequestMapping(path = "/api/inventory/{id}",method = RequestMethod.PUT)
    public void update(@PathVariable int id,@RequestBody InventoryForm form) throws ApiException {
        inventoryService.update(id,convert(form));
    }
    public InventoryData convert(InventoryPojo p) throws ApiException {
        InventoryData data=new InventoryData();
        data.setQuantity(p.getQuantity());
        ProductPojo productPojo=inventoryService.getProduct(p.getId());
        data.setProductName(productPojo.getProductName());
        data.setBarcode(productPojo.getBarcode());
        data.setId(productPojo.getId());
        return data;
    }
    public InventoryPojo convert(InventoryForm form) {
        InventoryPojo pojo=new InventoryPojo();
        pojo.setQuantity(form.getQuantity());
        pojo.setId(form.getId());
        return pojo;
    }
}
