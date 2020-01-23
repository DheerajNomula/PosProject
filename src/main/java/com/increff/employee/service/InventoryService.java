package com.increff.employee.service;
import com.increff.employee.dao.InventoryDao;
import com.increff.employee.pojo.InventoryPojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryDao dao;


    @Transactional(rollbackFor = ApiException.class)
    public void add(InventoryPojo p) throws ApiException {
            checkQuantity(p);
            dao.insert(p);
    }

    protected static void checkQuantity(InventoryPojo p) throws ApiException {
        if(p.getQuantity()<0)
            p.setQuantity(0);
    }

    @Transactional(rollbackFor = ApiException.class,readOnly = true)
    public InventoryPojo get(int id) throws ApiException {
        return getCheck(id);
    }

    @Transactional(readOnly = true)
    public List<InventoryPojo> getAll(){
        return dao.selectAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(int id,InventoryPojo p) throws ApiException {
        if(id!=p.getId())throw new ApiException("Id's should match in form and selected");
        checkQuantity(p);

        InventoryPojo newInventoryPojo=getCheck(id);

        newInventoryPojo.setQuantity(p.getQuantity());
        dao.update(newInventoryPojo);
    }

    @Transactional(rollbackFor = ApiException.class,readOnly = true)
    public InventoryPojo getCheck(int id) throws ApiException {
        InventoryPojo p=dao.select(id);
        ///checkProductId(id);
        if(p==null){
            throw new ApiException("Inventory with given Id does not exist, id: "+id);
        }
        return p;
    }

}