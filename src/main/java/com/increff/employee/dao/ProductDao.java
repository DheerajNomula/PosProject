package com.increff.employee.dao;

import com.increff.employee.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao{
    private static String select_id = "select p from ProductPojo p where id=:id";
    private static String select_all = "select p from ProductPojo p";
    private static String selectIdByBarcode="select p from ProductPojo p where barcode=:barcode ";

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void insert(ProductPojo p){
        em.persist(p);
    }

    public ProductPojo select(int id){
        TypedQuery<ProductPojo> query=getQuery(select_id,ProductPojo.class);
        query.setParameter("id",id);
        return getSingle(query);
    }

    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(select_all, ProductPojo.class);
        return query.getResultList();
    }

    public void update(ProductPojo p) {
    }

    public int selectIdByBarcode(String barcode) {
        TypedQuery<ProductPojo> query=getQuery(selectIdByBarcode,ProductPojo.class);
        query.setParameter("barcode",barcode);
        int id= getSingle(query).getId();
        return id;
    }
}
