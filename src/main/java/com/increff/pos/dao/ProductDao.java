package com.increff.pos.dao;

import com.increff.pos.pojo.ProductDisplayResult;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {
    private static String select_id = "select p from ProductPojo p where id=:id";
    private static String select_all = "select  new com.increff.pos.pojo.ProductDisplayResult(p.id,b.brandName,b.brandCategory,p.barcode,p.productName,p.mrp) from ProductPojo p,BrandPojo b where p.brandId=b.id";
    private static String selectIdByBarcode = "select p from ProductPojo p where barcode=:barcode ";
    private static String checkId = "select count(p) from ProductPojo p where id=:id";

    @Transactional
    public void insert(ProductPojo p) {
        em.persist(p);
    }

    public ProductPojo select(int id) {
        TypedQuery<ProductPojo> query = getQuery(select_id, ProductPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<ProductDisplayResult> selectAll() {
        Query query = em.createQuery(select_all);
        return query.getResultList();
    }

    public void update(ProductPojo p) {
    }

    public ProductPojo selectProductByBarcode(String barcode) {
        TypedQuery<ProductPojo> query = getQuery(selectIdByBarcode, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return getSingle(query);
    }

    public int countId(int id) {
        Query query = em.createQuery(checkId);
        query.setParameter("id", id);
        Number number = (Number) query.getSingleResult();
        int num = number.intValue();
        return num;
    }

}
