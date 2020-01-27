package com.increff.pos.dao;

import com.increff.pos.pojo.InventoryDisplayResult;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.InventoryReportResult;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.util.List;

@Repository
public class InventoryDao extends AbstractDao {
    private static String select_id = "select p from InventoryPojo p where id=:id";
    private static String select_all = "select new com.increff.pos.pojo.InventoryDisplayResult(p.barcode,i.quantity,p.productName,b.brandCategory,b.brandName,p.id) from InventoryPojo i,BrandPojo b,ProductPojo p where p.brandId=b.id and i.id=p.id";
    private static String getBrandAndQuantity = "select new com.increff.pos.pojo.InventoryReportResult(b.brandName,b.brandCategory,sum(i.quantity)) from BrandPojo b,ProductPojo p,InventoryPojo i where p.id=i.id and b.id=p.brandId " +
            "group by b.id";

    @Transactional
    public void insert(InventoryPojo p) {
        System.out.println(p.getQuantity());
        em.persist(p);
    }

    public InventoryPojo select(int id) {
        TypedQuery<InventoryPojo> query = getQuery(select_id, InventoryPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<InventoryDisplayResult> selectAll() {
        Query query = em.createQuery(select_all);
        return query.getResultList();
    }

    public void update(InventoryPojo p) {

    }

    public List<InventoryReportResult> InventoryReport() {
        Query query = em.createQuery(getBrandAndQuantity);
        return query.getResultList();
    }
}
