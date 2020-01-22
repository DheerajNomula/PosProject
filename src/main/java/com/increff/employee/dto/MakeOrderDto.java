package com.increff.employee.dto;

import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.*;
import com.increff.employee.util.Orders;
import org.apache.fop.apps.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    @Transactional(rollbackOn = ApiException.class)
    public void add(List<OrderForm> orderForms) throws ApiException {
        //int orderId=orderService.getLastOrder();//last order id +1 assuming no deletion of orders autowired so it will work
        //Date dateobj = new Date(Calendar.getInstance().getTime().getTime());
        Date dateobj=new Date();
        List<OrderItemPojo>orderItemPojos=convertToOrderitems(orderForms);// no errros

        OrderPojo orderPojo=new OrderPojo();
        orderPojo.setDate(dateobj);
        orderService.add(orderPojo);
        int orderId=orderPojo.getId();
        //int orderId=orderService.getIdByDate(dateobj);
        if(orderId<=0)
            throw new ApiException("Order with id :"+orderId+" doestn't exist");

        for (OrderItemPojo orderItemPojo:orderItemPojos) {
            orderItemPojo.setOrderId(orderId);
            //orderItemPojo.setSellingPrice();
            orderItemService.add(orderItemPojo); // first check the conditions and then make the order
        }

        generateXML(orderForms,orderPojo);
    }
    private void generateXML(List<OrderForm> orderForms,OrderPojo orderPojo) throws ApiException {
        List<OrderData> orderDatas=new ArrayList<OrderData>();
        double totalAmount=0;
        for(OrderForm orderForm:orderForms){
            OrderData orderData=convert(orderForm.getBarcode());
            orderData.setOrderId(orderPojo.getId());
            orderData.setQuantity(orderForm.getQuantity());
            orderDatas.add(orderData);
            totalAmount+=orderData.getMrp()*orderData.getQuantity();
        }
        Orders orders=new Orders(orderDatas);
        orders.setOrderId(orderPojo.getId());
        orders.setTotalAmount(totalAmount);//src/main/resources/com/increff/employee
        File file = new File("src/main/resources/com/increff/employee/orders.xml");
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(Orders.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(orders, file);
        } catch (Exception e) {
            throw new ApiException("Error while converting into pdf "+e);
        }
        //add total to xml
        generatePdf();
    }

    private void generatePdf() throws ApiException {
        try {
            File xmlfile = new File("src/main/resources/com/increff/employee/orders.xml");
            File xsltfile = new File("src/main/resources/com/increff/employee/orderStyle.xsl");
            File pdfDir = new File("src/main/resources/com/increff/employee/pdfs");
            pdfDir.mkdirs();
            File pdfFile = new File(pdfDir, "order.pdf");
            System.out.println(pdfFile.getAbsolutePath());
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

                fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

                // Setup XSLT
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));

                // Setup input for XSLT transformation
                Source src = new StreamSource(xmlfile);

                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());

                // Start XSLT transformation and FOP processing
                transformer.transform(src, res);
            } catch (FOPException | TransformerException e) {
                throw new ApiException("Error while converting into pdf "+e);
            } finally {
                out.close();
            }
        }catch(IOException | ApiException exp){
            throw new ApiException("Error while converting into pdf"+exp);
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
        orderItemPojo.setSellingPrice(productPojo.getMrp()*orderForm.getQuantity()); // changed to mrp*qty

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

}
