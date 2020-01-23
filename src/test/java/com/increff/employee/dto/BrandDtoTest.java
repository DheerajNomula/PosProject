package com.increff.employee.dto;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class BrandDtoTest extends AbstractUnitTest {
    @Autowired
    private BrandDto brandDto;
    //some of functionalities are already checked in service layer re-checking them ..integration testing
    @Autowired
    private BrandService brandService;
    private BrandData brandData;
    private BrandForm brandForm;

    @Before
    public void init(){
        brandForm=new BrandForm("lenovo","laptops");
        brandData=new BrandData();
    }

    @Test
    public void test_add_valid() throws ApiException {
        brandDto.add(brandForm);

        List<BrandPojo> list=brandService.getAll();
        Assert.assertEquals(brandForm.getBrandCategory(),(list.get(0)).getBrandCategory());
        Assert.assertEquals(brandForm.getBrandName(),(list.get(0)).getBrandName());
    }

    @Test
    public void test_convertformToPojo(){
        BrandPojo brandPojo=BrandDto.convert(brandForm);
        Assert.assertEquals(brandPojo.getBrandName(),brandForm.getBrandName());
        Assert.assertEquals(brandPojo.getBrandCategory(),brandForm.getBrandCategory());
    }

    @Test
    public void test_convertPojoToForm(){
        BrandForm brandForm=BrandDto.convert(new BrandPojo("lenovo","laptops"));
        Assert.assertEquals("lenovo",brandForm.getBrandName());
        Assert.assertEquals("laptops",brandForm.getBrandCategory());
    }

    @Test(expected = ApiException.class)
    public void test_add_invalid_duplicates() throws ApiException {
        brandDto.add(brandForm);
        brandDto.add(brandForm);
    }


    @Test(expected = ApiException.class)
    public void test_add_invalid_null() throws ApiException {
        brandForm.setBrandCategory("");
        brandDto.add(brandForm);
    }

    @Test
    public void test_get_valid() throws ApiException {
        BrandPojo brandPojo=new BrandPojo("lenovo","laptops");
        brandService.add(brandPojo);

        Assert.assertEquals(brandPojo.getBrandCategory(),brandDto.get(brandPojo.getId()).getBrandCategory());
        Assert.assertEquals(brandPojo.getBrandName(),brandDto.get(brandPojo.getId()).getBrandName());
    }

    @Test(expected = ApiException.class)
    public void test_get_invalid() throws ApiException {
        BrandPojo brandPojo=new BrandPojo("lenovo","laptops");
        brandService.add(brandPojo);
        brandDto.get(brandPojo.getId()+10);
    }

    @Test
    public void test_getAll_valid() throws ApiException {
        for(int i=0;i<5;i++){
            BrandPojo brandPojo=new BrandPojo("name "+i,"category "+i);
            brandService.add(brandPojo);
        }
        List<BrandData> list=brandDto.getAll();
        Assert.assertEquals(5,list.size());
        for(int i=0;i<5;i++){
            Assert.assertEquals("name "+i,list.get(i).getBrandName());
            Assert.assertEquals("category "+i,list.get(i).getBrandCategory());
        }
    }

    @Test
    public void test_getAll_invalid() {
        Assert.assertEquals(0,brandDto.getAll().size());
    }

    @Test
    public void test_update_valid() throws ApiException {
        BrandPojo brandPojo=new BrandPojo("lenovo","laptops");
        brandService.add(brandPojo);

        BrandForm brandForm1=new BrandForm("lenovo_new","laptops");
        brandDto.update(brandPojo.getId(),brandForm1);

        BrandPojo fromDb=brandService.get(brandPojo.getId());
        Assert.assertEquals(brandForm1.getBrandName(),fromDb.getBrandName());
        Assert.assertEquals(brandForm1.getBrandCategory(),fromDb.getBrandCategory());
    }

    @Test(expected = ApiException.class)
    public void test_update_invalid_Id() throws ApiException {
        BrandPojo brandPojo=new BrandPojo("lenovo_new","laptops");
        brandService.add(brandPojo);

        brandDto.update(brandPojo.getId()+10,brandForm);
    }

    @Test(expected = ApiException.class)
    public void test_update_invalid_name() throws ApiException {
        BrandPojo brandPojo=new BrandPojo("lenovo_new","laptops");
        brandService.add(brandPojo);

        brandForm.setBrandCategory("");
        brandDto.update(brandPojo.getId(),brandForm);
    }

    @Test(expected = ApiException.class)
    public void test_update_duplicates() throws ApiException {
        BrandPojo brandPojo=new BrandPojo("lenovo_new","laptops");
        brandService.add(brandPojo);
        BrandPojo brandPojo1=new BrandPojo("lenovo","laptops");
        brandService.add(brandPojo1);

        brandDto.update(brandPojo.getId(),brandForm);
    }

}
