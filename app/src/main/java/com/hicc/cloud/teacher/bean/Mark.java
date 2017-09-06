package com.hicc.cloud.teacher.bean;

/**
 * Created by 野 on 2016/10/13.
 */

import java.io.Serializable;

/**
 * 封装学生成绩数据
 */

public class Mark implements Serializable {
    private String course;
    private int mark;
    private String teacher;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
