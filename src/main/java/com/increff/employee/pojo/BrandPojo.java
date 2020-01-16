package com.increff.employee.pojo;


import javax.persistence.*;


@Entity
@Table(
		uniqueConstraints = @UniqueConstraint(columnNames = {"brandName","brandCategory"})
)
public class BrandPojo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)//auto increment id
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

	@Override
	public String toString() {
		return "BrandPojo{" +
				"id=" + id +
				", brandName='" + brandName + '\'' +
				", brandCategory='" + brandCategory + '\'' +
				'}';
	}
}
