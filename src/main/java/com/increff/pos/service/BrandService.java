package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandDao brandDao;

    protected static void normalize(BrandPojo brandPojo) {
        brandPojo.setBrandName(StringUtil.toLowerCase(brandPojo.getBrandName()));
        brandPojo.setBrandCategory(StringUtil.toLowerCase(brandPojo.getBrandCategory()));
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(BrandPojo brandPojo) throws ApiException {
        normalize(brandPojo);
        checkBrandNameAndCategory(brandPojo.getBrandName(), brandPojo.getBrandCategory());
        brandDao.insert(brandPojo);
    }

    protected void checkBrandNameAndCategory(String brandName, String brandCategory) throws ApiException {
        if (StringUtil.isEmpty(brandName)) {
            throw new ApiException("Brand name cannot be empty");
        }
        if (StringUtil.isEmpty(brandCategory)) {
            throw new ApiException("Brand category cannot be empty");
        }
        int exists = brandDao.checkBrandAndCategory(brandName, brandCategory);
        if (exists != 0)
            throw new ApiException("Brand name and category already exists");
    }

    @Transactional( readOnly = true)
    public BrandPojo get(int id)  {
        return brandDao.select(id);
    }

    @Transactional(readOnly = true)
    public List<BrandPojo> getAll() {
        return brandDao.selectAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(int id, BrandPojo brandPojo) throws ApiException {
        normalize(brandPojo);
        checkBrandNameAndCategory(brandPojo.getBrandName(), brandPojo.getBrandCategory());
        int checkDuplicate = brandDao.checkBrandAndCategory(brandPojo.getBrandName(), brandPojo.getBrandCategory());
        if (checkDuplicate != 0)
            throw new ApiException("Brand Name and Category already Exists");

        BrandPojo newBrandDao = getCheck(id);
        ;
        newBrandDao.setBrandName(brandPojo.getBrandName());
        newBrandDao.setBrandCategory(brandPojo.getBrandCategory());
    }

    @Transactional(rollbackFor = ApiException.class, readOnly = true)
    public BrandPojo getCheck(int id) throws ApiException {
        BrandPojo brandPojo = get(id);
        if (brandPojo == null) {
            throw new ApiException("Brand with given Id doesn't exist ,id =" + id);
        }

        return brandPojo;
    }

}
