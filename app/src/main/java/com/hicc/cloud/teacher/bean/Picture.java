package com.hicc.cloud.teacher.bean;

/**
 * Created by Administrator on 2016/9/29/029.
 */

public class Picture {
    private String title;
    private int imageId;

    public Picture(String title, int imageId) {
        this.title = title;
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
