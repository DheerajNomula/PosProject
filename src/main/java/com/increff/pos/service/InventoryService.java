package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryDisplayResult;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.InventoryReportResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryDao dao;

    protected static void checkQuantity(InventoryPojo p) {
        if (p.getQuantity() < 0)
            p.setQuantity(0);
    }

    @Transactional
    public void add(InventoryPojo p) {
        checkQuantity(p);
        InventoryPojo temp=dao.select(p.getId());
        if(temp==null)
        dao.insert(p);
        else{
            temp.setQuantity(temp.getQuantity()+p.getQuantity());
            dao.update(temp);
        }
    }

    @Transactional(rollbackFor = ApiException.class, readOnly = true)
    public InventoryPojo get(int id) throws ApiException {
        return dao.select(id);
    }

    @Transactional(readOnly = true)
    public List<InventoryDisplayResult> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(int id, InventoryPojo p) throws ApiException {
        if (id != p.getId()) throw new ApiException("Id's should match in form and selected");
        checkQuantity(p);

        InventoryPojo newInventoryPojo = getCheck(id);

        newInventoryPojo.setQuantity(p.getQuantity());
        dao.update(newInventoryPojo);
    }

    @Transactional(rollbackFor = ApiException.class, readOnly = true)
    public InventoryPojo getCheck(int id) throws ApiException {
        InventoryPojo p = get(id);
        if (p == null) {
            throw new ApiException("Inventory with given Id does not exist, id: " + id);
        }
        return p;
    }

    public List<InventoryReportResult> getInventoryReportData() {
        return dao.InventoryReport();
    }
}