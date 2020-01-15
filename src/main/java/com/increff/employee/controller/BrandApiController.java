package com.increff.employee.controller;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.model.EmployeeForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.EmployeePojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
//
@Api
@RestController
public class BrandApiController {
    @Autowired
    private BrandService brandService;

    @RequestMapping(path="/api/brand",method = RequestMethod.POST)
    @ApiOperation(value="Adds the brand")
    public void add(@RequestBody BrandForm brandForm)throws ApiException{
        BrandPojo brandPojo=convert(brandForm);
        brandService.add(brandPojo);
    }

    @ApiOperation(value = "Delete the brand")
    @RequestMapping(path = "/api/brand/{id}",method = RequestMethod.DELETE)
    public void delete(@PathVariable int id){
        brandService.delete(id);
    }

    @ApiOperation(value="Get the brand details")
    @RequestMapping(path="/api/brand/{id}",method = RequestMethod.GET)
    public BrandData get(@PathVariable int id) throws ApiException {
        BrandPojo brandPojo=brandService.get(id);
        return convert(brandPojo);
    }

    @ApiOperation(value = "Gets list of all brands")
    @RequestMapping(path = "/api/brand", method = RequestMethod.GET)
    public List<BrandData> getAll() {
        List<BrandPojo> listOfBrandService = brandService.getAll();
        List<BrandData> listOfBrandData = new ArrayList<BrandData>();
        for (BrandPojo p : listOfBrandService) {
            listOfBrandData.add(convert(p));
        }
        return listOfBrandData;
    }
    @ApiOperation(value = "Updates an brand")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody BrandForm f) throws ApiException {
        //System.out.println(f);
        BrandPojo p = convert(f);
        brandService.update(id, p);
    }
    /*@ApiOperation(value = "Get all Brands")
    @RequestMapping(path = "/api/brand/all", method = RequestMethod.GET)
    public List<String> getBrands() throws ApiException {
        //System.out.println(f);
        return brandService.getBrands();
    }*/
    private BrandData convert(BrandPojo brandPojo) {
        BrandData brandData=new BrandData();
        brandData.setBrandCategory(brandPojo.getBrandCategory());
        brandData.setBrandName(brandPojo.getBrandName());
        brandData.setId(brandPojo.getId());
        return brandData;
    }

    private BrandPojo convert(BrandForm brandForm) {
        BrandPojo brandPojo=new BrandPojo();
        brandPojo.setBrandName(brandForm.getBrandName());
        brandPojo.setBrandCategory(brandForm.getBrandCategory());
        return brandPojo;
    }
}
