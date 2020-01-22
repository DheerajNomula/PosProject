package com.increff.employee.service;

import com.increff.employee.pojo.BrandPojo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;


public class BrandServiceTest extends AbstractUnitTest{
    @Autowired
    private BrandService brandService;

    @Test
    public void testNormalize() {
        BrandPojo brandPojo=new BrandPojo("LENOVO  "," LApTops");
        brandService.normalize(brandPojo);
        Assert.assertEquals("lenovo",brandPojo.getBrandName());
        Assert.assertEquals("laptops",brandPojo.getBrandCategory());
    }

    @Test
    public void testNormalize_boundaryCase() {
        BrandPojo brandPojo=new BrandPojo("     ","   ");
        brandService.normalize(brandPojo);
        Assert.assertEquals("",brandPojo.getBrandName());
        Assert.assertEquals("",brandPojo.getBrandCategory());
    }

    @Test(expected = ApiException.class)
    public void testCheckIfEmpty_Name() throws ApiException {
        BrandPojo brandPojo=new BrandPojo("","abcd");
        brandService.checkIfEmpty(brandPojo.getBrandName(),brandPojo.getBrandCategory());
    }

    @Test(expected = ApiException.class)
    public void testCheckIfEmpty_Category() throws ApiException {
        BrandPojo brandPojo=new BrandPojo("abcd","");
        brandService.checkIfEmpty(brandPojo.getBrandName(),brandPojo.getBrandCategory());
    }


    @Test
    public void testGetCheck_valid() throws ApiException { //testing getcheck with passing valid data
        BrandPojo brandPojo=new BrandPojo("Lenovo","laptops");
        brandService.add(brandPojo);
        BrandPojo newbrandPojo=brandService.getCheck(brandPojo.getId());
        Assert.assertEquals("lenovo",brandPojo.getBrandName());
        Assert.assertEquals("laptops",brandPojo.getBrandCategory());
    }

    @Test(expected = ApiException.class)
    public void testGetCheck_invalid() throws ApiException { //testing getcheck with passing invalid data
        BrandPojo brandPojo=new BrandPojo("Lenovo","laptops");
        brandService.add(brandPojo);
        BrandPojo newbrandPojo=brandService.getCheck(15);
    }

    @Test
    public void testAddBrand() throws ApiException { //testing Add() with passing valid data
        BrandPojo actualBrand=new BrandPojo();
        actualBrand.setBrandCategory("laptops");
        actualBrand.setBrandName("Acer");
        brandService.add(actualBrand);
    }

    @Test
    public void testAddBrand_Name() throws ApiException { //testing Add() with passing valid data
        BrandPojo actualBrand=new BrandPojo();
        actualBrand.setBrandCategory("laptops");
        actualBrand.setBrandName("Acer");
        brandService.add(actualBrand);

        List<BrandPojo> brands=brandService.getAll();
        Assert.assertEquals(actualBrand.getBrandCategory(),brands.get(0).getBrandCategory());
        Assert.assertEquals(actualBrand.getBrandName(),brands.get(0).getBrandName());
    }

    @Test(expected = ApiException.class)
    public void testAddBrand_Duplicates() throws ApiException { //testing Add() with passing valid data(duplicate brand name and category)
        BrandPojo brand1=new BrandPojo("acer","laptops");
        brandService.add(brand1);
        BrandPojo brand2=new BrandPojo("acer","laptops");
        brandService.add(brand2);
    }

    @Test(expected = ApiException.class)
    public void testAddBrand_Null() throws ApiException { //testing Add() with passing invalid data(null values)
        BrandPojo brand1=new BrandPojo(); //redundant test case already checking the checkIfempty method
        brand1.setBrandName("acer");
        brandService.add(brand1);
    }

    @Test
    public void testGetAll() throws ApiException { //testing getAll()
        for(int i=0;i<5;i++){
            BrandPojo brandPojo=new BrandPojo("brand "+i,"category "+i);
            brandService.add(brandPojo);
        }
        List<BrandPojo> brandPojos=brandService.getAll();
        int i=0;
        for(BrandPojo brandPojo:brandPojos){
            Assert.assertEquals("brand "+i,brandPojo.getBrandName());
            Assert.assertEquals("category "+i,brandPojo.getBrandCategory());
            i++;
        }
        Assert.assertEquals(5,brandPojos.size());
    }

    @Test
    public void testGetAll_null() throws ApiException { //testing getAll()

        List<BrandPojo> brandPojos=brandService.getAll();
        Assert.assertEquals(0,brandPojos.size());
    }

    @Test
    public void testGet_valid_positive() throws ApiException {//testing get with passing valid data
        BrandPojo toDb=new BrandPojo("acer","laptops");
        brandService.add(toDb);
        BrandPojo fromDb=brandService.get(toDb.getId());
        Assert.assertEquals(toDb.getBrandName(),fromDb.getBrandName());
        Assert.assertEquals(toDb.getBrandCategory(),fromDb.getBrandCategory());
    }

    @Test
    public void testGet_valid_negative() throws ApiException {//testing Add() with passing invalid data
        BrandPojo toDb1=new BrandPojo("acer","laptops");
        BrandPojo toDb2=new BrandPojo("lenovo","laptops");
        brandService.add(toDb1);brandService.add(toDb2);
        BrandPojo fromDb2=brandService.get(toDb2.getId());
        Assert.assertNotEquals(toDb1.getBrandName(),fromDb2.getBrandName());
        Assert.assertEquals(toDb1.getBrandCategory(),fromDb2.getBrandCategory());
    }

    @Test(expected=ApiException.class)
    public void testGet_invalid() throws ApiException {
        BrandPojo toDb=new BrandPojo("acer","laptops");

        brandService.add(toDb);
        BrandPojo fromDb=brandService.get(toDb.getId()+1);
    }

    @Test
    public void testUpdate_valid() throws ApiException {
        BrandPojo brandPojo=new BrandPojo("brand ","category");
        brandService.add(brandPojo);
        BrandPojo newBrand=new BrandPojo("leNOvo","lapTOps");

        brandService.update(brandPojo.getId(),newBrand);
        BrandPojo dbBrand=brandService.get(brandPojo.getId());
        Assert.assertEquals(newBrand.getBrandCategory(),dbBrand.getBrandCategory());
        Assert.assertEquals(newBrand.getBrandName(),dbBrand.getBrandName());
    }

    @Test(expected = ApiException.class)
    public void testUpdate_invalid_Id() throws ApiException{
        BrandPojo newBrand=new BrandPojo("lenovo","laptops");
        brandService.add(newBrand);
        brandService.update(newBrand.getId()+1,newBrand);
    }

    @Test(expected = ApiException.class)
    public void testUpdate_invalid_existingBrand() throws ApiException{ //updating the brand name and category to the existing brand
        BrandPojo toDb0=new BrandPojo("brand 0","category 0");
        BrandPojo toDb1=new BrandPojo("brand 1","category 1");
        brandService.add(toDb1);brandService.add(toDb0);

        BrandPojo newBrand=new BrandPojo("brand 0","category 0");
        brandService.update(toDb1.getId(),newBrand);
    }

    @Test(expected = ApiException.class)
    public void testUpdate_invalid_null() throws ApiException{ //updating the brand name or category null
        BrandPojo toDb0=new BrandPojo("brand 0","category 0");
        BrandPojo toDb1=new BrandPojo("brand 1","category 1");
        brandService.add(toDb1);brandService.add(toDb0);

        BrandPojo newBrand=new BrandPojo("","category 0");
        brandService.update(toDb1.getId(),newBrand);
    }
}
