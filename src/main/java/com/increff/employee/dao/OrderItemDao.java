package com.increff.employee.dao;

import com.increff.employee.model.SalesForm;
import com.increff.employee.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao{
    private static String select_id="select p from OrderItemPojo p where id=:id";
    private static String select_all="select p from OrderItemPojo p";
    private static String selectByOrderId="select p from OrderItemPojo p where p.orderId=:id";
    private static String getProductBtwDates ="select o.productId,sum(o.quantity),sum(o.sellingPrice) from OrderItemPojo o  where o.orderId in " +
            "(select p.id from OrderPojo p where p.date between :startDate and :endDate) group by o.productId";


    @Transactional
    public void insert(OrderItemPojo orderItemPojo){
        em.persist(orderItemPojo);
    }

    public OrderItemPojo select(int id){
        TypedQuery<OrderItemPojo> query=getQuery(select_id,OrderItemPojo.class);
        query.setParameter("id",id);
        return getSingle(query);
    }

    public List<OrderItemPojo> selectAll(){
        TypedQuery<OrderItemPojo> query=em.createQuery(select_all,OrderItemPojo.class);
        return query.getResultList();
    }

    public void update(OrderItemPojo pojo){}


    public List<OrderItemPojo> getByOrderId(int id) {
        TypedQuery<OrderItemPojo> query=em.createQuery(selectByOrderId,OrderItemPojo.class);
        query.setParameter("id",id);
        return query.getResultList();
    }

    public List<Object[]> salesReport(SalesForm salesForm) {
        Query query=em.createNativeQuery(getProductBtwDates);
        query.setParameter("startDate",salesForm.getStartDate());
        query.setParameter("endDate",salesForm.getEndDate());
        List<Object[]> list=query.getResultList();
        return list;
    }
}
