package com.increff.pos.dto;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryDisplayResult;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void add(InventoryForm form) throws ApiException {
        InventoryPojo p = convert(form);
        checkProductId(p);
        inventoryService.add(p);
    }

    @Transactional
    public InventoryData get(int id) throws ApiException {
        InventoryPojo p = inventoryService.getCheck(id);
        return convert(p);
    }

    public List<InventoryData> getAll() throws ApiException {
        List<InventoryDisplayResult> list = inventoryService.getAll();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for (InventoryDisplayResult p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    private InventoryData convert(InventoryDisplayResult p) {
        InventoryData data = new InventoryData();
        data.setQuantity(p.getQuantity());


        data.setProductName(p.getProductName());
        data.setBarcode(p.getBarcode());
        data.setId(p.getId());
        data.setBrandCategory(p.getBrandCategory());
        data.setBrandName(p.getBrandName());

        return data;
    }

    public void update(int id, InventoryForm form) throws ApiException {

        inventoryService.update(id, convert(form));
    }

    protected InventoryData convert(InventoryPojo p) throws ApiException {
        InventoryData data = new InventoryData();
        data.setQuantity(p.getQuantity());

        ProductPojo productPojo = getProduct(p.getId());

        data.setProductName(productPojo.getProductName());
        data.setBarcode(productPojo.getBarcode());
        data.setId(productPojo.getId());
        BrandPojo brandPojo = getBrand(productPojo.getBrandId());

        data.setBrandCategory(brandPojo.getBrandCategory());
        data.setBrandName(brandPojo.getBrandName());

        return data;
    }

    public BrandPojo getBrand(int brandId) throws ApiException {
        return brandService.getCheck(brandId);
    }

    protected InventoryPojo convert(InventoryForm form) throws ApiException {
        InventoryPojo pojo = new InventoryPojo();

        pojo.setQuantity(form.getQuantity());
        pojo.setId(getProductId(form.getBarcode()));
        return pojo;
    }

    public ProductPojo getProduct(int productId) throws ApiException {
        return productService.getCheck(productId);
    }

    public int getProductId(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getProductByBarcode(barcode);
        if (productPojo == null)
            throw new ApiException("Product with corresponding barcode doesn't exist");
        return productPojo.getId();
    }


    protected void checkProductId(InventoryPojo inventoryPojo) throws ApiException {
        if (productService.checkId(inventoryPojo.getId()) == 0)
            throw new ApiException("Product Id does not exists in product table");
    }

}
