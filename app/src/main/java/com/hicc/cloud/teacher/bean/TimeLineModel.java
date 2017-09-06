package com.hicc.cloud.teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/13/026.
 */
public class TimeLineModel implements Serializable{
    private String time;
    private String scool;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getScool() {
        return scool;
    }

    public void setScool(String scool) {
        this.scool = scool;
    }
}
