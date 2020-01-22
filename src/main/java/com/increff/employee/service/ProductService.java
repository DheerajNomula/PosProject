package com.increff.employee.service;

import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;


    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo productPojo) throws ApiException {
        normalize(productPojo);
        check(productPojo);
        if(getProductByBarcode(productPojo.getBarcode())!=null)
            throw new ApiException("Barcode already exists");
        productDao.insert(productPojo);
    }

    protected void check(ProductPojo productPojo) throws ApiException {
        if (StringUtil.isEmpty(productPojo.getProductName())) {
            throw new ApiException("Product Name cannot be empty");
        }
        if (StringUtil.isEmpty(productPojo.getBarcode())) {
            throw new ApiException("Product Barcode cannot be empty");
        }
        if(productPojo.getMrp()<=0)
            throw new ApiException("Enter valid Mrp");
        //if(checkBrand(productPojo)){

    }

    protected void normalize(ProductPojo productPojo) {
        productPojo.setProductName(StringUtil.toLowerCase(productPojo.getProductName()));
        productPojo.setBarcode(StringUtil.toLowerCase(productPojo.getBarcode()));
    }

    @Transactional
    public ProductPojo get(int id) throws ApiException {
        return getCheck(id);
    }
    public List<ProductPojo> getAll(){
        return productDao.selectAll();
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int id,ProductPojo productPojo) throws ApiException{
        normalize(productPojo);
        check(productPojo);

        ProductPojo newProductPojo=getCheck(id);
        if(!newProductPojo.getBarcode().equalsIgnoreCase(productPojo.getBarcode())) {
            if(getProductByBarcode(productPojo.getBarcode())!=null)
            throw new ApiException("Invalid Barcode");
        }
        newProductPojo.setProductName(productPojo.getProductName());
        newProductPojo.setBarcode(productPojo.getBarcode());
        newProductPojo.setBrandId(productPojo.getBrandId());
        newProductPojo.setMrp(productPojo.getMrp());
    }

    @Transactional(rollbackOn = ApiException.class)
    protected ProductPojo getCheck(int id) throws ApiException {
        ProductPojo productPojo= productDao.select(id);
        if(productPojo==null){
            throw new ApiException("Product with given Id doesn't exist ,id ="+id);
        }
        return productPojo;
    }

    @Transactional
    public ProductPojo getProductByBarcode(String barcode) {
        return productDao.selectProductByBarcode(barcode);
    }

    //return 1 if id exists 0 if it doesn't
    @Transactional
    public int checkId(int id) {
        return productDao.countId(id);
    }

}
