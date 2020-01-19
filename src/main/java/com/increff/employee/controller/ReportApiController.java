package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import com.increff.employee.dto.ReportsDto;
import com.increff.employee.model.SalesForm;
import com.increff.employee.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
    @RequestMapping(path = "/api/reports/sales", method = RequestMethod.GET)
    public List<SalesData> get(@RequestBody SalesForm salesForm) throws ApiException {
        SalesData p = service.get(salesForm);
        return convert(p);
    }

    @ApiOperation(value = "Gets list of all employees")
    @RequestMapping(path = "/api/employee", method = RequestMethod.GET)
    public List<EmployeeData> getAll() {
        List<EmployeePojo> list = service.getAll();
        List<EmployeeData> list2 = new ArrayList<EmployeeData>();
        for (EmployeePojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    @ApiOperation(value = "Updates an employee")
    @RequestMapping(path = "/api/employee/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody EmployeeForm f) throws ApiException {
        EmployeePojo p = convert(f);
        service.update(id, p);
    }


    private static EmployeeData convert(EmployeePojo p) {
        EmployeeData d = new EmployeeData();
        d.setAge(p.getAge());
        d.setName(p.getName());
        d.setId(p.getId());
        return d;
    }

    private static EmployeePojo convert(EmployeeForm f) {
        EmployeePojo p = new EmployeePojo();
        p.setAge(f.getAge());
        p.setName(f.getName());
        return p;
    }

}
