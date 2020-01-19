package com.increff.employee.dao;

import com.increff.employee.pojo.OrderPojo;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao{
    private static String select_id="select p from OrderPojo p where id=:id";
    private static String select_all="select p from OrderPojo p";
    private static String getIdByDate="select p.id from OrderPojo p where date=:date";
    private static String ordersBetweenDates="select p.id from OrderPojo p where p.date between :startDate and :endDate";
    @PersistenceContext
    EntityManager em;

    @Transactional
    public OrderPojo insert(OrderPojo orderPojo){
        em.persist(orderPojo);
        return orderPojo;
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

    public int getIdByDate(Date dateobj) {
        Query query=em.createQuery(getIdByDate);
        query.setParameter("date",dateobj);
        Number number=(Number)query.getSingleResult();
        int num=number.intValue();
        System.out.println("last order value: "+num);
        return num;
    }

    public List<Integer> getOrdersBetween(Date startDate, Date endDate) {
        TypedQuery<Integer> query=em.createQuery(ordersBetweenDates,Integer.class);
        query.setParameter("startDate",startDate);
        query.setParameter("endDate",endDate);
        List<Integer> list=query.getResultList();
        return list;
    }
}
