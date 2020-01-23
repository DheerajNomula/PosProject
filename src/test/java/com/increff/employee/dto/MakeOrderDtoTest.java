package com.increff.employee.dto;

import com.increff.employee.model.OrderForm;
import com.increff.employee.pojo.*;
import com.increff.employee.service.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MakeOrderDtoTest extends AbstractUnitTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private MakeOrderDto makeOrderDto;

    private BrandPojo brandPojo;
    private InventoryPojo inventoryPojo;
    private ProductPojo productPojo;
    @Before
    public void init() throws ApiException {
        brandPojo=new BrandPojo("lenovo","laptops");
        brandService.add(brandPojo);
        productPojo=new ProductPojo("laptop123","thinkpad",25000,brandPojo.getId());
        productService.add(productPojo);
        inventoryPojo=new InventoryPojo(productPojo.getId(),20);
        inventoryService.add(inventoryPojo);
    }

    @Test
    public void testAdd() throws ApiException {
        OrderForm orderForm=new OrderForm("laptop123",10);
        List<OrderForm> list=new ArrayList<>();
        list.add(orderForm);
        makeOrderDto.add(list);

        List<OrderPojo>  allOrders=orderService.getAll();
        List<OrderItemPojo> orderItems= orderItemService.getByOrderId(allOrders.get(0).getId());
        Assert.assertEquals(orderForm.getQuantity(),(orderItems.get(0)).getQuantity());
        Assert.assertEquals(productService.getProductByBarcode(orderForm.getBarcode()).getId(),(orderItems.get(0)).getProductId());
    }

    @Test(expected = ApiException.class)
    public void testAdd_invalid_quanity() throws ApiException {
        OrderForm orderForm=new OrderForm("laptop123",-10);
        List<OrderForm> list=new ArrayList<>();
        list.add(orderForm);
        makeOrderDto.add(list);
    }

    @Test(expected = ApiException.class)
    public void testAdd_invalid_quantityMore() throws ApiException {
        OrderForm orderForm=new OrderForm("laptop123",100);
        List<OrderForm> list=new ArrayList<>();
        list.add(orderForm);
        makeOrderDto.add(list);
    }

    @Test(expected = ApiException.class)
    public void testAdd_invalid_barcode() throws ApiException {
        OrderForm orderForm=new OrderForm("abcd",100);
        List<OrderForm> list=new ArrayList<>();
        list.add(orderForm);
        makeOrderDto.add(list);
    }

    @Test
    public void testAdd_duplicate_barcode() throws ApiException {
        OrderForm orderForm=new OrderForm("laptop123",10);
        OrderForm orderForm1=new OrderForm("laptop123",10);
        List<OrderForm> list=new ArrayList<>();
        list.add(orderForm);list.add(orderForm);
        makeOrderDto.add(list);

        List<OrderPojo>  allOrders=orderService.getAll();
        List<OrderItemPojo> orderItems= orderItemService.getByOrderId(allOrders.get(0).getId());
        Assert.assertEquals(1,orderItems.size());
        Assert.assertEquals(20,orderItems.get(0).getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testAdd_duplicate_barcode_exceedsQty() throws ApiException {
        OrderForm orderForm=new OrderForm("laptop123",20);
        OrderForm orderForm1=new OrderForm("laptop123",10);
        List<OrderForm> list=new ArrayList<>();
        list.add(orderForm);list.add(orderForm);
        makeOrderDto.add(list);
    }


}
