package com.increff.employee.dto;

import com.google.protobuf.Api;
import com.increff.employee.model.BrandData;
import com.increff.employee.model.InventoryData;
import com.increff.employee.model.SalesData;
import com.increff.employee.model.SalesForm;
import com.increff.employee.pojo.*;
import com.increff.employee.service.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportsDto {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    public List<SalesData> getSales(SalesForm salesForm) throws ApiException {

        System.out.println(salesForm);
        List<Object[]> listAns=orderItemService.salesReport(salesForm);
        List<SalesData> salesDataList=new ArrayList<SalesData>();
        //Map with key category and value as SalesData
        Map<String,SalesData> map=new HashMap<String,SalesData>();
        for(Object[] obj:listAns){ // 0 - product Id , 1- total quantity , 2- total selling price
            SalesData salesData=new SalesData();
            //System.out.println(obj[0]+" "+obj[1]+" "+obj[2]);
            ProductPojo productPojo=productService.get((Integer) obj[0]);
            BrandPojo brandPojo=brandService.get(productPojo.getBrandId());
            Number number=(Number)obj[1];
            if(salesForm.getBrandCategory().isEmpty()||brandPojo.getBrandCategory().equalsIgnoreCase(salesForm.getBrandCategory())){
                if(salesForm.getBrandName().isEmpty() || salesForm.getBrandName().equalsIgnoreCase(brandPojo.getBrandName())){
                    if(map.containsKey(brandPojo.getBrandCategory()) == true){
                        SalesData temp=map.get(brandPojo.getBrandCategory());
                        temp.setQuantity(temp.getQuantity()+number.intValue());
                        temp.setRevenue(temp.getRevenue()+(Double) obj[2]);
                        map.put(brandPojo.getBrandCategory(),temp);
                    }
                    else {
                        SalesData newData=new SalesData();
                        newData.setQuantity(number.intValue());
                        //newData.setBrandName(brandPojo.getBrandName());
                        newData.setBrandCategory(brandPojo.getBrandCategory());
                        newData.setRevenue((Double)obj[2]);
                        map.put(newData.getBrandCategory(),newData);
                    }
                }
            }
        }
        for(Map.Entry m:map.entrySet()){
            salesDataList.add((SalesData) m.getValue());
        }
        return salesDataList;
    }

    public List<InventoryData> getInventory() throws ApiException {
        List<InventoryPojo> inventoryPojos=inventoryService.getAll();
        Map<Integer,InventoryData> map=new HashMap<Integer, InventoryData>();
        for(InventoryPojo inventoryPojo:inventoryPojos){
            ProductPojo productPojo=productService.get(inventoryPojo.getId());
            BrandPojo brandPojo=brandService.get(productPojo.getBrandId());
            if(map.containsKey(brandPojo.getId()) == true){
                InventoryData temp=map.get(brandPojo.getId());
                temp.setQuantity(temp.getQuantity()+inventoryPojo.getQuantity());
                map.put(brandPojo.getId(),temp);
            }
            else {
                InventoryData newData=new InventoryData();
                newData.setQuantity(inventoryPojo.getQuantity());
                newData.setBrandName(brandPojo.getBrandName());
                newData.setBrandCategory(brandPojo.getBrandCategory());
                map.put(brandPojo.getId(), newData);
            }
        }

        List<InventoryData> inventoryDataList=new ArrayList<InventoryData>();
        for(Map.Entry m:map.entrySet()){
            inventoryDataList.add((InventoryData) m.getValue());
            //System.out.println(m.getValue());
        }
        return inventoryDataList;
    }

    public List<BrandPojo> getBrand() {
        return brandService .getAll();
    }

}
