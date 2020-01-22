package com.increff.employee.dto;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProducDtoTest extends AbstractUnitTest {
    @Autowired
    private ProductDto productDto;
    @Autowired
    private BrandService brandService;
    @Before
    public void init() throws ApiException {
        BrandPojo brandPojo=new BrandPojo("lenovo","laptops");
        brandService.add(brandPojo);
    }

    @Test(expected = ApiException.class)
    public void test_checkBrand() throws ApiException {
        productDto.getBrand(2);
    }
}
