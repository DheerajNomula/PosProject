package com.increff.employee.service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandDao brandDao;

    @Transactional(rollbackFor = ApiException.class)
    public void add(BrandPojo brandPojo) throws ApiException {
        normalize(brandPojo);
        checkBrandNameAndCategory(brandPojo.getBrandName(),brandPojo.getBrandCategory());
        brandDao.insert(brandPojo);
    }

    protected void checkBrandNameAndCategory(String brandName, String brandCategory) throws ApiException {
        if(StringUtil.isEmpty(brandName)){
            throw new ApiException("Brand name cannot be empty");
        }
        if(StringUtil.isEmpty(brandCategory)){
            throw new ApiException("Brand category cannot be empty");
        }
        int exists=brandDao.checkBrandAndCategory(brandName,brandCategory);
        if(exists!=0)
            throw new ApiException("Brand name and category already exists");
    }
    protected static void normalize(BrandPojo brandPojo)  {
        brandPojo.setBrandName(StringUtil.toLowerCase(brandPojo.getBrandName()));
        brandPojo.setBrandCategory(StringUtil.toLowerCase(brandPojo.getBrandCategory()));
    }

    @Transactional(rollbackFor = ApiException.class,readOnly = true)
    public BrandPojo get(int id) throws ApiException {
        return getCheck(id);
    }
    @Transactional(readOnly = true)
    public List<BrandPojo> getAll(){
        return brandDao.selectAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(int id,BrandPojo brandPojo) throws ApiException {
        normalize(brandPojo);
        checkBrandNameAndCategory(brandPojo.getBrandName(),brandPojo.getBrandCategory());
        int checkDuplicate=brandDao.checkBrandAndCategory(brandPojo.getBrandName(),brandPojo.getBrandCategory());
        if(checkDuplicate!=0)
            throw new ApiException("Brand Name and Category already Exists");

        BrandPojo newBrandDao=getCheck(id);;
        newBrandDao.setBrandName(brandPojo.getBrandName());
        newBrandDao.setBrandCategory(brandPojo.getBrandCategory());
    }

    @Transactional(rollbackFor = ApiException.class,readOnly = true)
    protected BrandPojo getCheck(int id) throws ApiException {
        BrandPojo brandPojo= brandDao.select(id);
        if(brandPojo==null){
            throw new ApiException("Brand with given Id doesn't exist ,id ="+id);
        }

        return brandPojo;
    }

}
