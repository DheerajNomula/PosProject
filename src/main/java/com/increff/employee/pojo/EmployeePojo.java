package com.increff.employee.pojo;


import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class EmployeePojo {
    @Id
    private int id;
    private int age;
    private String name;

    public EmployeePojo(){}
    public EmployeePojo(int id,String name,int age){
        this.id=id;this.name=name;this.age=age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
