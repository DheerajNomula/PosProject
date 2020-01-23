package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.ApiException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class BrandDao extends AbstractDao {
    private static String select_id = "select p from BrandPojo p where id=:id";
    private static String select_all = "select p from BrandPojo p";
    private static String select_Allbrands="select distinct a.brandName from BrandPojo a";
    private static String checkNameAndCateogry="select count(*) from BrandPojo p where brandName=:brandName and brandCategory=:brandCategory";


    public void insert(BrandPojo p) throws ApiException {
        int id=checkBrandAndCategory(p.getBrandName(),p.getBrandCategory());
        if(id!=0)
            throw new ApiException("Brand Name And Category Already Exists");
        em.persist(p);
    }

    //No need of transactional for select query
    public BrandPojo select(int id) {

        TypedQuery<BrandPojo> query = getQuery(select_id, BrandPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<BrandPojo> selectAll() {
        TypedQuery<BrandPojo> query = getQuery(select_all, BrandPojo.class);
        return query.getResultList();
    }

    public void update(BrandPojo p) {
    }

    public List<String> selectBrands() {
        Query query=em.createQuery(select_Allbrands);
        return query.getResultList();
    }

    public int checkBrandAndCategory(String brandName, String brandCategory) {
        Query query=em.createQuery(checkNameAndCateogry);
        query.setParameter("brandName",brandName);
        query.setParameter("brandCategory",brandCategory);
        Number number=(Number)query.getSingleResult();
        int num=number.intValue();
        return num;
    }

    public int checkId(int brandId) {
        return 0;
    }
}
