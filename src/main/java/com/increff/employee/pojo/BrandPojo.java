package com.increff.employee.pojo;

import com.increff.employee.dao.AbstractDao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BrandPojo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String brandName;
	private String brandCategory;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandCategory() {
		return brandCategory;
	}

	public void setBrandCategory(String brandCategory) {
		this.brandCategory = brandCategory;
	}
}
