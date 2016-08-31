package com.myscript.atk.math.sample.entity;

import java.io.Serializable;

/**
 * Created by hd_chen on 2016/8/30.
 */
public class Ad implements Serializable{
    String url;
    String img;

    public Ad() {
    }

    public Ad(String url, String img) {
        this.url = url;
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Ad{" +
                "url='" + url + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
