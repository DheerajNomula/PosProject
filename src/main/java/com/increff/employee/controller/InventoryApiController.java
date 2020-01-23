package com.increff.employee.controller;

import com.increff.employee.dto.InventoryDto;
import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InventoryApiController {
    
    @Autowired
    private InventoryDto inventoryDto;
    
    @ApiOperation(value = "Adds the inventory")
    @RequestMapping(path = "/api/inventory",method = RequestMethod.POST)
    public void add(@RequestBody InventoryForm form) throws ApiException {
        inventoryDto.add(form);
    }
    @ApiOperation(value = "Gets the inventory")
    @RequestMapping(path="/api/inventory/{id}",method = RequestMethod.GET)
    public InventoryData get(@PathVariable int id) throws ApiException {
        return inventoryDto.get(id);
    }

    @ApiOperation(value = "Gets list of all inventory")
    @RequestMapping(path="/api/inventory",method = RequestMethod.GET)
    public List<InventoryData> getAll() throws ApiException {
        return inventoryDto.getAll();

    }
    @ApiOperation(value="Update the inventory")
    @RequestMapping(path = "/api/inventory/{id}",method = RequestMethod.PUT)
    public void update(@PathVariable int id,@RequestBody InventoryForm form) throws ApiException {
        System.out.println(form);
        inventoryDto.update(id,form);
    }

}
