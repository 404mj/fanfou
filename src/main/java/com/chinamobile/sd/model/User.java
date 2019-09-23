package com.chinamobile.sd.model;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/22 8:24
 */
public class User {
    private String name;
    private Integer age;
    private String company;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}
