package com.increff.employee.dto;

import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MakeOrderDto {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    ProductService productService;

    @Autowired
    InventoryService inventoryService;

    private che

    public void add(OrderForm orderForm) throws ApiException {
        orderItemService.add(convertToOrderitem(orderForm)); // first check the conditions and then make the order
    }

    private OrderItemPojo convertToOrderitem(OrderForm orderForm) throws ApiException {
        OrderItemPojo orderItemPojo=new OrderItemPojo();
        orderItemPojo.setQuantity(orderForm.getQuantity());

        ProductPojo productPojo=productService.getProductByBarcode(orderForm.getBarcode());
        if(productPojo==null)
            throw new ApiException("Product doesn't exist ,barcode :"+orderForm.getBarcode());
        orderItemPojo.setProductId(productPojo.getId());
        orderItemPojo.setSellingPrice(orderForm.getMrp());

        InventoryPojo inventoryPojo=inventoryService.get(productPojo.getId());
        if(inventoryPojo.getQuantity()<orderForm.getQuantity())
            throw new  ApiException("Out Of Stock ! Available : "+inventoryPojo.getQuantity()+" but wanted : "+orderForm.getQuantity());
        // no errors then order is made
        orderService.add(convertToOrder(orderForm));
        inventoryPojo.setQuantity(inventoryPojo.getQuantity()-orderForm.getQuantity());
        inventoryService.update(productPojo.getId(),inventoryPojo);
        int orderId=orderService.getLastOrder();//last order id +1 assuming no deletion of orders autowired so it will work
        if(orderId<=0)
            throw new ApiException("Order with id :"+orderId+" doestn't exist");
        orderItemPojo.setOrderId(orderId);
        return orderItemPojo;
    }

    private OrderPojo convertToOrder(OrderForm orderForm) {
        OrderPojo orderPojo=new OrderPojo();
        //orderPojo.setId(); autoincrement
        orderPojo.setDate(orderForm.getDate());
        return orderPojo;
    }

    public OrderData get(int id) throws ApiException {
        return convert(orderItemService.get(id));
    }

    private OrderData convert(OrderItemPojo orderItemPojo) throws ApiException {
        OrderData orderData=new OrderData();

        ProductPojo productPojo=productService.get(orderItemPojo.getProductId());
        if(productPojo==null)
            throw new ApiException("Product doesn't exist ,Id :"+orderItemPojo.getProductId());

        orderData.setProductName(productPojo.getProductName());
        orderData.setBarcode(productPojo.getBarcode());

        OrderPojo orderPojo=orderService.get(orderItemPojo.getId());
        if(orderPojo==null)
            throw new ApiException("Order with id "+orderItemPojo.getOrderId()+" doesn't exist");
        orderData.setDate(orderPojo.getDate());
        orderData.setMrp(productPojo.getMrp()); // keeping the product mrp
        orderData.setQuantity(orderItemPojo.getQuantity());

        return orderData;
    }

    public List<OrderData> getAll() throws ApiException {
        List<OrderData> orderDatas=new ArrayList<OrderData>();
        List<OrderItemPojo> orderPojos=orderItemService.getAll();
        for(OrderItemPojo orderItemPojo:orderPojos)
        {
            orderDatas.add(convert(orderItemPojo));
        }
        return orderDatas;
    }

    public void update(int id,OrderForm f) throws ApiException {
        orderItemService.update(id,convertToOrderitem(f));
    }
}
