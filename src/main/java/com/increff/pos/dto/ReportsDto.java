package com.increff.pos.dto;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.SalesData;
import com.increff.pos.model.SalesForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryReportResult;
import com.increff.pos.pojo.SalesReportResult;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportsDto {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BrandService brandService;

    public List<SalesData> getSalesData(SalesForm salesForm) throws ApiException {
        List<SalesReportResult> listAns = orderItemService.salesReport(salesForm);
        List<SalesData> list = new ArrayList<>();
        for (SalesReportResult obj : listAns) {
            SalesData salesData = new SalesData();
            salesData.setBrandCategory(obj.getBrandCategory());
            salesData.setQuantity((int) obj.getQuantity());
            salesData.setRevenue(obj.getSellingPrice());
            list.add(salesData);
        }
        return list;
    }

    public List<InventoryData> getInventoryData() {
        List<InventoryReportResult> reportRows = inventoryService.getInventoryReportData();
        List<InventoryData> inventoryDataList = new ArrayList<>();
        for (InventoryReportResult row : reportRows) {
            InventoryData temp = new InventoryData();
            temp.setBrandName(row.getBrandName());
            temp.setBrandCategory(row.getBrandCategory());
            temp.setQuantity((int) row.getQuantity());
            inventoryDataList.add(temp);
        }
        return inventoryDataList;
    }

    public List<BrandPojo> getBrand() {
        return brandService.getAll();
    }

}
