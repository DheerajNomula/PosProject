package com.increff.pos.util;

import com.increff.pos.model.OrderData;

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

    public Orders() {
    }

    public Orders(List<OrderData> list) {
        this.list = list;
    }

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

    public List<OrderData> getList() {
        return list;
    }

    public void setList(List<OrderData> list) {
        this.list = list;
    }
}
