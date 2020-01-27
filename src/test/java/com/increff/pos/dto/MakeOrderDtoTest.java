package com.increff.pos.dto;

import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
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
        brandPojo = new BrandPojo("lenovo", "laptops");
        brandService.add(brandPojo);
        productPojo = new ProductPojo("laptop123", "thinkpad", 25000, brandPojo.getId());
        productService.add(productPojo);
        inventoryPojo = new InventoryPojo(productPojo.getId(), 20);
        inventoryService.add(inventoryPojo);
    }

    @Test
    public void testAdd() throws ApiException {
        OrderForm orderForm = new OrderForm("laptop123", 10);
        List<OrderForm> list = new ArrayList<>();
        list.add(orderForm);
        makeOrderDto.add(list);

        List<OrderPojo> allOrders = orderService.getAll();
        List<OrderItemPojo> orderItems = orderItemService.getByOrderId(allOrders.get(0).getId());
        Assert.assertEquals(orderForm.getQuantity(), (orderItems.get(0)).getQuantity());
        Assert.assertEquals(productService.getProductByBarcode(orderForm.getBarcode()).getId(), (orderItems.get(0)).getProductId());
    }

    @Test(expected = ApiException.class)
    public void testAdd_invalid_quanity() throws ApiException {
        OrderForm orderForm = new OrderForm("laptop123", -10);
        List<OrderForm> list = new ArrayList<>();
        list.add(orderForm);
        makeOrderDto.add(list);
    }

    @Test(expected = ApiException.class)
    public void testAdd_invalid_quantityMore() throws ApiException {
        OrderForm orderForm = new OrderForm("laptop123", 100);
        List<OrderForm> list = new ArrayList<>();
        list.add(orderForm);
        makeOrderDto.add(list);
    }

    @Test(expected = ApiException.class)
    public void testAdd_invalid_barcode() throws ApiException {
        OrderForm orderForm = new OrderForm("abcd", 100);
        List<OrderForm> list = new ArrayList<>();
        list.add(orderForm);
        makeOrderDto.add(list);
    }

    @Test
    public void testAdd_duplicate_barcode() throws ApiException {
        OrderForm orderForm = new OrderForm("laptop123", 10);
        OrderForm orderForm1 = new OrderForm("laptop123", 10);
        List<OrderForm> list = new ArrayList<>();
        list.add(orderForm);
        list.add(orderForm);
        makeOrderDto.add(list);

        List<OrderPojo> allOrders = orderService.getAll();
        List<OrderItemPojo> orderItems = orderItemService.getByOrderId(allOrders.get(0).getId());
        Assert.assertEquals(1, orderItems.size());
        Assert.assertEquals(20, orderItems.get(0).getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testAdd_duplicate_barcode_exceedsQty() throws ApiException {
        OrderForm orderForm = new OrderForm("laptop123", 20);
        OrderForm orderForm1 = new OrderForm("laptop123", 10);
        List<OrderForm> list = new ArrayList<>();
        list.add(orderForm);
        list.add(orderForm);
        makeOrderDto.add(list);
    }

    @Test(expected = ApiException.class)
    public void testAdd_duplicate_barcode_oneNegative() throws ApiException {
        OrderForm orderForm = new OrderForm("laptop123", 20);
        OrderForm orderForm1 = new OrderForm("laptop123", -10);
        List<OrderForm> list = new ArrayList<>();
        list.add(orderForm);
        list.add(orderForm);
        makeOrderDto.add(list);

        List<OrderPojo> allOrders = orderService.getAll();
        List<OrderItemPojo> orderItems = orderItemService.getByOrderId(allOrders.get(0).getId());
        Assert.assertEquals(1, orderItems.size());
        Assert.assertEquals(10, orderItems.get(0).getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testAdd_duplicate_barcode_oneNegative1() throws ApiException {
        OrderForm orderForm = new OrderForm("laptop123", -20);
        OrderForm orderForm1 = new OrderForm("laptop123", 10);
        List<OrderForm> list = new ArrayList<>();
        list.add(orderForm);
        list.add(orderForm);
        makeOrderDto.add(list);

        List<OrderPojo> allOrders = orderService.getAll();
        List<OrderItemPojo> orderItems = orderItemService.getByOrderId(allOrders.get(0).getId());
        Assert.assertEquals(1, orderItems.size());
        Assert.assertEquals(0, orderItems.get(0).getQuantity());
    }

    @Test
    public void test_checkForDuplicates() {
        List<OrderForm> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new OrderForm("laptop123", 10));
        }
        list = MakeOrderDto.checkForDuplicates(list);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("laptop123", list.get(0).getBarcode());
        Assert.assertEquals(50, list.get(0).getQuantity());
    }

    @Test
    public void test_get_valid() throws ApiException {
        OrderData orderData = makeOrderDto.get("laptop123");
        Assert.assertEquals("thinkpad", orderData.getProductName());
        Assert.assertEquals(20, orderData.getQuantity());
    }

    @Test
    public void test_getAll_zeroSize() throws ApiException {
        Assert.assertEquals(0, makeOrderDto.getAll().size());
    }

    @Test
    public void test_getAll_valid() throws ApiException {
        for (int i = 0; i < 5; i++) {
            ProductPojo productPojo = new ProductPojo("barcode " + i, "product " + i, i + 100, i);
            productService.add(productPojo);
            OrderPojo order = new OrderPojo(new Date());
            orderService.add(order);
            OrderItemPojo orderItemPojo = new OrderItemPojo(order.getId(), productPojo.getId(), 10, 100 + i);
            orderItemService.add(orderItemPojo);
        }
        List<OrderData> list = makeOrderDto.getAll();
        Assert.assertEquals(5, list.size());
    }
}
