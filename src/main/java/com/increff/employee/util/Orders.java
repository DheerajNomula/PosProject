package com.increff.employee.util;

import com.increff.employee.model.OrderData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "orders")
public class Orders {
    @XmlElement(name = "order")
    private List<OrderData> list;
    private int orderId;
    private double totalAmount;

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Orders() {
    }

    public Orders(List<OrderData> list) {
        this.list = list;
    }

    public List<OrderData> getList() {
        return list;
    }

    public void setList(List<OrderData> list) {
        this.list = list;
    }
}
