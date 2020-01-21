package com.increff.employee.dto;

import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InventoryDto {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    public void add( InventoryForm form) throws ApiException {
        InventoryPojo p=convert(form);
        if(!checkProductId(p)) {
            inventoryService.add(p);
        }
    }
    
    public InventoryData get(int id) throws ApiException {
        InventoryPojo p=inventoryService.get(id);
        return convert(p);
    }

    public List<InventoryData> getAll() throws ApiException {
        List<InventoryPojo> list=inventoryService.getAll();
        List<InventoryData> list2=new ArrayList<InventoryData>();
        for(InventoryPojo p:list){
            list2.add(convert(p));
        }
        return list2;
    }

    public void update( int id, InventoryForm form) throws ApiException {
        inventoryService.update(id,convert(form));
    }
    
    public InventoryData convert(InventoryPojo p) throws ApiException {
        InventoryData data=new InventoryData();
        data.setQuantity(p.getQuantity());

        ProductPojo productPojo=getProduct(p.getId());

        data.setProductName(productPojo.getProductName());
        data.setBarcode(productPojo.getBarcode());
        data.setId(productPojo.getId());
        BrandPojo brandPojo=getBrand(productPojo.getBrandId());

        data.setBrandCategory(brandPojo.getBrandCategory());
        data.setBrandName(brandPojo.getBrandName());

        return data;
    }
    public BrandPojo getBrand(int brandId) throws ApiException {
        return brandService.get(brandId);
    }

    public InventoryPojo convert(InventoryForm form) {
        InventoryPojo pojo=new InventoryPojo();

        pojo.setQuantity(form.getQuantity());
        pojo.setId(getProductId(form.getBarcode()));
        return pojo;
    }
    public ProductPojo getProduct(int productId) throws ApiException {
        return productService.get(productId);
    }

    public int getProductId(String barcode) {
        ProductPojo productPojo=productService.getProductByBarcode(barcode);
        return productPojo.getId();
    }

    public boolean checkProductId(InventoryPojo inventoryPojo) throws ApiException {
        if(productService.checkId(inventoryPojo.getId())==0)
            throw new ApiException("Product Id does not exists in product table");
        try {
            InventoryPojo existingPojo=inventoryService.get(inventoryPojo.getId());
            System.out.println("Product Id already exists in inventory table");
            existingPojo.setQuantity(existingPojo.getQuantity()+inventoryPojo.getQuantity());
            inventoryService.update(inventoryPojo.getId(),existingPojo);
            return true;
        }
        catch (ApiException e) {
            return false;
        }

    }

}
