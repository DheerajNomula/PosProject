package com.increff.pos.dto;

import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.Orders;
import com.increff.pos.service.*;
import org.apache.fop.apps.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.*;

@Service
public class MakeOrderDto {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    protected static List<OrderForm> checkForDuplicates(List<OrderForm> orderForms) {
        Map<String, Integer> map = new HashMap<>();
        List<OrderForm> newList = new ArrayList<>();
        for (OrderForm orderForm : orderForms) {
            if (map.containsKey(orderForm.getBarcode())) {
                map.put(orderForm.getBarcode(), orderForm.getQuantity() + map.get(orderForm.getBarcode()));
            } else {
                map.put(orderForm.getBarcode(), orderForm.getQuantity());
            }
        }
        for (Map.Entry m : map.entrySet()) {
            OrderForm orderForm = new OrderForm((String) m.getKey(), (int) m.getValue());
            newList.add(orderForm);
        }
        return newList;
    }

    protected static void generatePdf(ByteArrayOutputStream xmlbaos) throws ApiException {
        try {
            //File xmlfile = new File("src/main/resources/com/increff/pos/orders.xml");
            File xsltfile = new File("src/main/resources/com/increff/pos/orderStyle.xsl");
            File pdfDir = new File("src/main/resources/com/increff/pos/pdfs");
            pdfDir.mkdirs();
            File pdfFile = new File(pdfDir, "order.pdf");
            //System.out.println(pdfFile.getAbsolutePath());
            // configure fopFactory as desired
            final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // configure foUserAgent as desired

            // Setup output
            OutputStream out = new FileOutputStream(pdfFile);
            out = new java.io.BufferedOutputStream(out);
            try {
                // Construct fop with desired output format
                Fop fop;
                //ByteArrayOutputStream pdfbaos=new ByteArrayOutputStream();

                fop = fopFactory.newFop(MimeConstants.MIME_PDF,out);

                // Setup XSLT
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));

                // Setup input for XSLT transformation
                Source src = new StreamSource(new ByteArrayInputStream(xmlbaos.toByteArray()));

                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());

                // Start XSLT transformation and FOP processing
                transformer.transform(src, res);
            } catch (FOPException | TransformerException e) {
                throw new ApiException("Error while converting into pdf " + e);
            } finally {
                out.close();
            }
        } catch (IOException | ApiException exp) {
            throw new ApiException("Error while converting into pdf" + exp);
        }
    }

    protected static OrderData addProductToOrderData(ProductPojo productPojo) {
        OrderData orderData = new OrderData();
        orderData.setMrp(productPojo.getMrp());
        orderData.setBarcode(productPojo.getBarcode());
        orderData.setProductName(productPojo.getProductName());
        return orderData;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void add(List<OrderForm> orderForms) throws ApiException {

        Date dateobj = new Date();
        orderForms = checkForDuplicates(orderForms);
        List<OrderItemPojo> orderItemPojos = convertToOrderitems(orderForms);// no errros

        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setDate(dateobj);
        orderService.add(orderPojo);
        int orderId = orderPojo.getId();
        //int orderId=orderService.getIdByDate(dateobj);
        if (orderId <= 0)
            throw new ApiException("Order with id :" + orderId + " doestn't exist");

        for (OrderItemPojo orderItemPojo : orderItemPojos) {
            orderItemPojo.setOrderId(orderId);
            //orderItemPojo.setSellingPrice();
            orderItemService.add(orderItemPojo); // first check the conditions and then make the order
        }

        generateXML(orderForms, orderPojo);
    }

    protected void generateXML(List<OrderForm> orderForms, OrderPojo orderPojo) throws ApiException {
        List<OrderData> orderDatas = new ArrayList<OrderData>();
        double totalAmount = 0;
        for (OrderForm orderForm : orderForms) {
            OrderData orderData = convert(orderForm.getBarcode());
            orderData.setOrderId(orderPojo.getId());
            orderData.setQuantity(orderForm.getQuantity());
            orderDatas.add(orderData);
            totalAmount += orderData.getMrp() * orderData.getQuantity();
        }
        Orders orders = new Orders(orderDatas);
        orders.setOrderId(orderPojo.getId());
        orders.setTotalAmount(totalAmount);//src/main/resources/com/increff/pos
        File file = new File("src/main/resources/com/increff/pos/orders.xml");
        JAXBContext jaxbContext = null;

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        StreamResult streamResult=new StreamResult(baos); // xml data

        try {
            jaxbContext = JAXBContext.newInstance(Orders.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(orders, streamResult);

        } catch (Exception e) {
            throw new ApiException("Error while converting into pdf " + e);
        }
        //add total to xml
        generatePdf(baos);
    }

    protected List<OrderItemPojo> convertToOrderitems(List<OrderForm> orderForms) throws ApiException {
        List<OrderItemPojo> orderItemPojos = new ArrayList<OrderItemPojo>();
        for (OrderForm orderForm : orderForms) {
            orderItemPojos.add(convertToOrderitem(orderForm));
        }
        return orderItemPojos;
    }

    protected OrderItemPojo convertToOrderitem(OrderForm orderForm) throws ApiException {
        //orderId is not set
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        if (orderForm.getQuantity() < 0)
            orderForm.setQuantity(0);
        orderItemPojo.setQuantity(orderForm.getQuantity());

        ProductPojo productPojo = productService.getProductByBarcode(orderForm.getBarcode());
        if (productPojo == null)
            throw new ApiException("Product doesn't exist ,barcode :" + orderForm.getBarcode());
        orderItemPojo.setProductId(productPojo.getId());
        orderItemPojo.setSellingPrice(productPojo.getMrp() * orderForm.getQuantity()); // changed to mrp*qty

        InventoryPojo inventoryPojo = inventoryService.getCheck(productPojo.getId());
        if (inventoryPojo.getQuantity() < orderForm.getQuantity())
            throw new ApiException("Out Of Stock ! Available : " + inventoryPojo.getQuantity() + " but wanted : " + orderForm.getQuantity());
        // no errors then order is made

        inventoryPojo.setQuantity(inventoryPojo.getQuantity() - orderForm.getQuantity());
        inventoryService.update(productPojo.getId(), inventoryPojo);

        return orderItemPojo;
    }

    public OrderData get(String barcode) throws ApiException {
        return convert(barcode);//orderItemPOjo to orderdata
    }

    protected OrderData convert(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getProductByBarcode(barcode);
        if (productPojo == null)
            throw new ApiException("Product doesn't exist ,barcode :" + barcode);
        InventoryPojo inventoryPojo = inventoryService.getCheck(productPojo.getId());

        OrderData orderData = addProductToOrderData(productPojo);
        orderData.setQuantity(inventoryPojo.getQuantity());
        return orderData;
    }

    @Transactional
    public List<OrderData> getAll() throws ApiException {
        List<OrderPojo> orderPojos = orderService.getAll();

        List<OrderData> orderDatas = new ArrayList<OrderData>();

        for (OrderPojo orderPojo : orderPojos) {
            List<OrderItemPojo> orderItemPojos = orderItemService.getByOrderId(orderPojo.getId());
            for (OrderItemPojo orderItemPojo : orderItemPojos) {
                ProductPojo productPojo = productService.getCheck(orderItemPojo.getProductId());
                OrderData orderData = addProductToOrderData(productPojo);
                orderData.setOrderId(orderPojo.getId());
                orderData.setQuantity(orderItemPojo.getQuantity());
                orderDatas.add(orderData);    // add orderId vise
            }
        }
        return orderDatas;
    }


}
