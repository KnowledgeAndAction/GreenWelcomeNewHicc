package com.hicc.cloud.teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/13/013.
 * 家庭表
 */

public class Family implements Serializable {
    private int id;
    // 学生学号
    private String studentNum;
    // 姓名
    private String name;
    // 工作
    private String workand;
    // 关系
    private String relation;
    // 电话
    private String phone;
    // 年龄
    private int age;
    // 政治面貌
    private String politics;
    // 通讯地址
    private String contactAddress;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkand() {
        return workand;
    }

    public void setWorkand(String workand) {
        this.workand = workand;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPolitics() {
        return politics;
    }

    public void setPolitics(String politics) {
        this.politics = politics;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }
}
