package com.hicc.cloud.teacher.bean;

/**
 * Created by Administrator on 2016/10/13/013.
 * 学部表
 */

public class Division {
    private int id;
    private String divisionDes;
    private int divisionCode;
    private int gradeCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDivisionDes() {
        return divisionDes;
    }

    public void setDivisionDes(String divisionDes) {
        this.divisionDes = divisionDes;
    }

    public int getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(int divisionCode) {
        this.divisionCode = divisionCode;
    }

    public int getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(int gradeCode) {
        this.gradeCode = gradeCode;
    }
}
