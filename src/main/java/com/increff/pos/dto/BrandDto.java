package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandDto {
    @Autowired
    private BrandService brandService;

    protected static BrandData convert(BrandPojo brandPojo) {
        BrandData brandData = new BrandData();
        brandData.setBrandCategory(brandPojo.getBrandCategory());
        brandData.setBrandName(brandPojo.getBrandName());
        brandData.setId(brandPojo.getId());
        return brandData;
    }

    protected static BrandPojo convert(BrandForm brandForm) {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrandName(brandForm.getBrandName());
        brandPojo.setBrandCategory(brandForm.getBrandCategory());
        return brandPojo;
    }

    public void add(BrandForm brandForm) throws ApiException {
        BrandPojo brandPojo = convert(brandForm);
        brandService.add(brandPojo);
    }

    public BrandData get(int id) throws ApiException {
        BrandPojo brandPojo = brandService.getCheck(id);
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

    public void update(int id, BrandForm f) throws ApiException {
        //System.out.println(f);
        BrandPojo p = convert(f);
        brandService.update(id, p);
    }
}

