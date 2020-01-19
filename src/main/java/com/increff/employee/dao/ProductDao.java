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
    private static String checkId="select count(p) from ProductPojo p where id=:id";
    private static String selectIdByBrand="select p.id from ProductPojo where brandId:brandId";
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

    public ProductPojo selectProductByBarcode(String barcode) {
        TypedQuery<ProductPojo> query=getQuery(selectIdByBarcode,ProductPojo.class);
        query.setParameter("barcode",barcode);
         return getSingle(query);
    }

    public int countId(int id) {
        Query query=em.createQuery(checkId);
        query.setParameter("id",id);
        Number number=(Number)query.getSingleResult();
        int num=number.intValue();
        return num;
    }

    public List<Integer> getIdsByBrand(int brandId) {
        TypedQuery<Integer> query=getQuery(selectIdByBrand,Integer.class);
        query.setParameter("brandId",brandId);
        List<Integer> list= query.getResultList();
        for(Integer integer:list){
            System.out.println(integer.intValue());
        }
        return list;
    }
}
