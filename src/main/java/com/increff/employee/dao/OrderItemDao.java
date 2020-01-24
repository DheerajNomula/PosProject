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
    private static String getProductBtwDates ="select b.brandName,b.brandCategory,sum(o.quantity),sum(o.sellingPrice) from OrderItemPojo o,BrandPojo b,ProductPojo p  where p.brandId=b.id and p.id=o.productId and o.orderId in (select ord.id from OrderPojo ord where ord.date between :startDate and :endDate) group by b.id";

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

    protected List<Object[]> executeSalesQuery(String salesquery,SalesForm salesForm){
        Query query=em.createNativeQuery(salesquery);
        query.setParameter("startDate",salesForm.getStartDate());
        query.setParameter("endDate",salesForm.getEndDate());
        return query.getResultList();
    }
    public List<Object[]>  salesReport(SalesForm salesForm) {

        if(salesForm.getBrandName().length()==0)
        {
            if(salesForm.getBrandCategory().length()==0)
            return executeSalesQuery(getProductBtwDates,salesForm);
            else {
                String query=getProductBtwDates + " having b.brandCategory='" +salesForm.getBrandCategory()+"'";
                return executeSalesQuery(query,salesForm);
            }
        }
        else {
            //brand given
            String query=getProductBtwDates+" having b.brandName='"+salesForm.getBrandName()+"'";
            if(salesForm.getBrandCategory().length()==0)
                return executeSalesQuery(query,salesForm);
            else {
                query += " and b.brandCategory='" +salesForm.getBrandCategory()+"'";
                return executeSalesQuery(query,salesForm);
            }
        }
    }
}
