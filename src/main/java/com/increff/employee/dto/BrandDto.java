package com.increff.employee.dto;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandDto {
    @Autowired
    private BrandService brandService;

    public void add(BrandForm brandForm)throws ApiException {
        BrandPojo brandPojo=convert(brandForm);
        brandService.add(brandPojo);
    }

    public BrandData get( int id) throws ApiException {
        BrandPojo brandPojo=brandService.get(id);
        return convert(brandPojo);
    }

    public List<BrandData> getAll() {
        List<BrandPojo> listOfBrandService = brandService.getAll();
        List<BrandData> listOfBrandData = new ArrayList<BrandData>();
        for (BrandPojo p : listOfBrandService) {
            listOfBrandData.add(convert(p));
        }
        return listOfBrandData;
    }
   
    public void update( int id,  BrandForm f) throws ApiException {
        //System.out.println(f);
        BrandPojo p = convert(f);
        brandService.update(id, p);
    }
    
    private static BrandData convert(BrandPojo brandPojo) {
        BrandData brandData=new BrandData();
        brandData.setBrandCategory(brandPojo.getBrandCategory());
        brandData.setBrandName(brandPojo.getBrandName());
        brandData.setId(brandPojo.getId());
        return brandData;
    }

    private static BrandPojo convert(BrandForm brandForm) {
        BrandPojo brandPojo=new BrandPojo();
        brandPojo.setBrandName(brandForm.getBrandName());
        brandPojo.setBrandCategory(brandForm.getBrandCategory());
        return brandPojo;
    }
}

