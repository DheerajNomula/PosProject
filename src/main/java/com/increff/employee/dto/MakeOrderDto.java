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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    @Transactional(rollbackOn = ApiException.class)
    public void add(List<OrderForm> orderForms) throws ApiException {
        //int orderId=orderService.getLastOrder();//last order id +1 assuming no deletion of orders autowired so it will work
        //Date dateobj = new Date(Calendar.getInstance().getTime().getTime());
        Date dateobj=new Date();
        List<OrderItemPojo>orderItemPojos=convertToOrderitems(orderForms);// no errros

        OrderPojo orderPojo=new OrderPojo();
        orderPojo.setDate(dateobj);
        OrderPojo updatedOrderPojo=orderService.add(orderPojo);
        int orderId=orderPojo.getId();
        //int orderId=orderService.getIdByDate(dateobj);
        if(orderId<=0)
            throw new ApiException("Order with id :"+orderId+" doestn't exist");

        for (OrderItemPojo orderItemPojo:orderItemPojos) {
            orderItemPojo.setOrderId(orderId);
            orderItemService.add(orderItemPojo); // first check the conditions and then make the order
        }
    }
    private List<OrderItemPojo> convertToOrderitems(List<OrderForm> orderForms) throws ApiException {
        List<OrderItemPojo> orderItemPojos=new ArrayList<OrderItemPojo>();
        for(OrderForm orderForm:orderForms){
            orderItemPojos.add(convertToOrderitem(orderForm));
        }
        return orderItemPojos;
    }
    private OrderItemPojo convertToOrderitem(OrderForm orderForm) throws ApiException {
        //orderId is not set
        OrderItemPojo orderItemPojo=new OrderItemPojo();
        orderItemPojo.setQuantity(orderForm.getQuantity());
        //orderItemPojo.setOrderId(orderId);

        ProductPojo productPojo=productService.getProductByBarcode(orderForm.getBarcode());
        if(productPojo==null)
            throw new ApiException("Product doesn't exist ,barcode :"+orderForm.getBarcode());
        orderItemPojo.setProductId(productPojo.getId());
        orderItemPojo.setSellingPrice(productPojo.getMrp());

        InventoryPojo inventoryPojo=inventoryService.get(productPojo.getId());
        if(inventoryPojo.getQuantity()<orderForm.getQuantity())
            throw new  ApiException("Out Of Stock ! Available : "+inventoryPojo.getQuantity()+" but wanted : "+orderForm.getQuantity());
        // no errors then order is made

        inventoryPojo.setQuantity(inventoryPojo.getQuantity()-orderForm.getQuantity());
        inventoryService.update(productPojo.getId(),inventoryPojo);

        return orderItemPojo;
    }



    public OrderData get(String barcode) throws ApiException {
        return convert(barcode);//orderItemPOjo to orderdata
    }
    private OrderData addProductToOrderData(ProductPojo productPojo){
        OrderData orderData=new OrderData();
        orderData.setMrp(productPojo.getMrp());
        orderData.setBarcode(productPojo.getBarcode());
        orderData.setProductName(productPojo.getProductName());
        return orderData;
    }
    private OrderData convert(String barcode) throws ApiException {
        ProductPojo productPojo=productService.getProductByBarcode(barcode);
        if(productPojo==null)
            throw new ApiException("Product doesn't exist ,barcode :"+barcode);
        InventoryPojo inventoryPojo=inventoryService.get(productPojo.getId());

        OrderData orderData=addProductToOrderData(productPojo);
        orderData.setQuantity(inventoryPojo.getQuantity());
        return orderData;
    }

    public List<OrderData> getAll() throws ApiException {
        List<OrderPojo> orderPojos=orderService.getAll();

        List<OrderData> orderDatas=new ArrayList<OrderData>();

        for(OrderPojo orderPojo:orderPojos)
        {
            List<OrderItemPojo> orderItemPojos=orderItemService.getByOrderId(orderPojo.getId());
            for(OrderItemPojo orderItemPojo:orderItemPojos){
                ProductPojo productPojo=productService.get(orderItemPojo.getProductId());
                OrderData orderData=addProductToOrderData(productPojo);
                orderData.setOrderId(orderPojo.getId());
                orderData.setQuantity(orderItemPojo.getQuantity());
                orderDatas.add(orderData);    // add orderId vise
            }
        }
        return orderDatas;
    }

    /*public List<OrderData> getAll() throws ApiException {
        List<OrderData> orderDatas=new ArrayList<OrderData>();
        List<OrderItemPojo> orderPojos=orderItemService.getAll();
        for(OrderItemPojo orderItemPojo:orderPojos)
        {
            orderDatas.add(convert(orderItemPojo));
        }
        return orderDatas;
    }*/

    /*public void update(int id,OrderForm f) throws ApiException {
        orderItemService.update(id,convertToOrderitem(f));
    }
*/

}
