package com.hicc.cloud.teacher.bean;

/**
 * Created by Administrator on 2016/10/13/013.
 * 专业表
 */

public class Professional {
    private int id;
    private String professionalDes;
    private int professionalCode;
    private int divisionCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfessionalDes() {
        return professionalDes;
    }

    public void setProfessionalDes(String professionalDes) {
        this.professionalDes = professionalDes;
    }

    public int getProfessionalCode() {
        return professionalCode;
    }

    public void setProfessionalCode(int professionalCode) {
        this.professionalCode = professionalCode;
    }

    public int getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(int divisionCode) {
        this.divisionCode = divisionCode;
    }
}
