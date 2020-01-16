package com.increff.employee.controller;

import com.increff.employee.dto.BrandDto;
import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//
@Api
@RestController
public class BrandApiController {
    @Autowired
    private BrandDto brandDto;

    @RequestMapping(path="/api/brand",method = RequestMethod.POST)
    @ApiOperation(value="Adds the brand")
    public void add(@RequestBody BrandForm brandForm)throws ApiException{
        brandDto.add(brandForm);
    }

    @ApiOperation(value = "Delete the brand")
    @RequestMapping(path = "/api/brand/{id}",method = RequestMethod.DELETE)
    public void delete(@PathVariable int id){
        brandDto.delete(id);
    }

    @ApiOperation(value="Get the brand details")
    @RequestMapping(path="/api/brand/{id}",method = RequestMethod.GET)
    public BrandData get(@PathVariable int id) throws ApiException {
        return brandDto.get(id);
    }

    @ApiOperation(value = "Gets list of all brands")
    @RequestMapping(path = "/api/brand", method = RequestMethod.GET)
    public List<BrandData> getAll() {
        return brandDto.getAll();
    }
    @ApiOperation(value = "Updates an brand")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody BrandForm f) throws ApiException {
        brandDto.update(id, f);
    }


}
