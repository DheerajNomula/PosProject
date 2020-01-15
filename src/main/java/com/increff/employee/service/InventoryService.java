package com.increff.employee.service;
import com.increff.employee.dao.InventoryDao;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import javax.transaction.Transactional;
@Service
public class InventoryService {
    @Autowired
    private InventoryDao dao;

    @Autowired
    private ProductService productService;

    @Transactional(rollbackOn = ApiException.class)
    public void add(InventoryPojo p) throws ApiException {
        checkQuantity(p);
        dao.insert(p);
    }

    private void checkQuantity(InventoryPojo p) throws ApiException {
        if(p.getQuantity()<0)
            throw new ApiException("Quantity cannot be negative");
    }
    public InventoryPojo get(int id) throws ApiException {
        return getCheck(id);
    }

    public List<InventoryPojo> getAll(){
        return dao.selectAll();
    }
    @Transactional(rollbackOn = ApiException.class)
    public void update(int id,InventoryPojo p) throws ApiException {
        checkQuantity(p);
        InventoryPojo newInventoryPojo=getCheck(id);
        newInventoryPojo.setQuantity(p.getQuantity());
        //newInventoryPojo.setId(p.getId()); or id passed in url
        dao.update(newInventoryPojo);
    }

    @Transactional
    public InventoryPojo getCheck(int id) throws ApiException {
        InventoryPojo p=dao.select(id);
        if(p==null){
            throw new ApiException("Inventory with given Id does not exist, id: "+id);
        }
        return p;
    }

    public ProductPojo getProduct(int productId) throws ApiException {
        return productService.get(productId);
    }

    public int getId(String barcode) {
        return productService.getIdByBarcode(barcode);
    }
}
