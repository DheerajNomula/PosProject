package com.increff.employee.service;

import com.google.protobuf.Api;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    BrandService brandService;
    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo productPojo) throws ApiException {
        normalize(productPojo);
        if (StringUtil.isEmpty(productPojo.getProductName())) {
            throw new ApiException("Product Name cannot be empty");
        }
        if (StringUtil.isEmpty(productPojo.getBarcode())) {
            throw new ApiException("Product Barcode cannot be empty");
        }
        if(productPojo.getMrp()<0)
            throw new ApiException("Mrp Should be positive");
        if(checkBrand(productPojo)){
            productDao.insert(productPojo);
        }
    }
    private boolean checkBrand(ProductPojo p) throws ApiException {

        List<BrandPojo> brandPojos=brandService.getAll();
        for(BrandPojo brandPojo:brandPojos){
            if(brandPojo.getId()==p.getBrandId())
                return true;
        }
        throw new ApiException("Brand with id:"+p.getBrandId()+" doestn't exist ");
    }
    private void normalize(ProductPojo productPojo) {
        productPojo.setProductName(StringUtil.toLowerCase(productPojo.getProductName()));
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo get(int id) throws ApiException {
        return getCheck(id);
    }
    public List<ProductPojo> getAll(){
        return productDao.selectAll();
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int id,ProductPojo productPojo) throws ApiException{
        normalize(productPojo);
        checkBrand(productPojo);
        ProductPojo newProductPojo=getCheck(id);
        newProductPojo.setProductName(productPojo.getProductName());
        newProductPojo.setBarcode(productPojo.getBarcode());
        newProductPojo.setBrandId(productPojo.getBrandId());
        newProductPojo.setMrp(productPojo.getMrp());
    }

    @Transactional(rollbackOn = ApiException.class)
    private ProductPojo getCheck(int id) throws ApiException {
        ProductPojo productPojo= productDao.select(id);
        if(productPojo==null){
            throw new ApiException("Product with given Id doesn't exist ,id ="+id);
        }

        return productPojo;
    }

    @Transactional(rollbackOn = ApiException.class)
    public BrandPojo getBrand(int brandId) throws ApiException {
        BrandPojo brandPojo=brandService.get(brandId);
        return brandPojo;
    }

    @Transactional
    public int getIdByBarcode(String barcode) {
        return productDao.selectIdByBarcode(barcode);
    }

    @Transactional
    public int checkId(int id) {
        return productDao.countId(id);
    }
}
