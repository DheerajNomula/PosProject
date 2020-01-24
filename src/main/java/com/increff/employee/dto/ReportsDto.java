package com.increff.employee.dto;

import com.increff.employee.model.InventoryData;
import com.increff.employee.model.SalesData;
import com.increff.employee.model.SalesForm;
import com.increff.employee.pojo.*;
import com.increff.employee.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportsDto {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BrandService brandService;

    @Autowired
    public List<SalesData> getSalesData(SalesForm salesForm) throws ApiException {
        List<Object[]> listAns=orderItemService.salesReport(salesForm);
        List<SalesData> list=new ArrayList<>();
        for(Object[] obj:listAns) { // 0-brandName , 1-brandCategory, 2-qty ,3-selling price
            SalesData salesData = new SalesData();
            salesData.setBrandName(obj[0].toString());
            salesData.setBrandCategory(obj[1].toString());
            Number number = (Number) obj[2];
            salesData.setQuantity(number.intValue());

            salesData.setRevenue((Double)obj[3]);
            list.add(salesData);
        }
        return list;
    }
    public List<InventoryData> getInventoryData(){ //0-brandName , 1-brandCategory, 2-qty
        List<Object[]> reportRows=inventoryService.getInventoryReportData();
        List<InventoryData> inventoryDataList=new ArrayList<>();
        for(Object[] row:reportRows){
                InventoryData temp=new InventoryData();
                temp.setBrandName(row[0].toString());
                temp.setBrandCategory(row[1].toString());
                Number number=(Number)row[2];
                temp.setQuantity(number.intValue());
                inventoryDataList.add(temp);
        }
        return inventoryDataList;
    }

    public List<BrandPojo> getBrand() {
        return brandService .getAll();
    }

}
