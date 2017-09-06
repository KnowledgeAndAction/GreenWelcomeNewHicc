package com.hicc.cloud.teacher.bean;

/**
 * Created by Administrator on 2016/11/5/005.
 */

public class StartStudent {
    String name;
    long phone;
    int classId;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }
}
