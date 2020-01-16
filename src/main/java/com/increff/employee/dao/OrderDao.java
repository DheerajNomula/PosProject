package com.increff.employee.dao;

import com.increff.employee.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao{
    private static String select_id="select p from OrderPojo p where id=:id";
    private static String select_all="select p from OrderPojo p";
    private static String get_lastId="select max(p.id) from OrderPojo p";
    @PersistenceContext
    EntityManager em;

    @Transactional
    public void insert(OrderPojo OrderPojo){
        em.persist(OrderPojo);
    }

    public OrderPojo select(int id){
        TypedQuery<OrderPojo> query=getQuery(select_id,OrderPojo.class);
        query.setParameter("id",id);
        return getSingle(query);
    }

    public List<OrderPojo> selectAll(){
        TypedQuery<OrderPojo> query=em.createQuery(select_all,OrderPojo.class);
        return query.getResultList();
    }

    public void update(OrderPojo pojo){}


    public int getLastOrder() {
        Query query=em.createQuery(get_lastId);
        Number number=(Number)query.getSingleResult();
        int num=number.intValue();
        System.out.println("last order value: "+num);
        return num;
    }
}
