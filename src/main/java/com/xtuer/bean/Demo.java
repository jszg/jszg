package com.xtuer.bean;

import java.io.Serializable;

public class Demo {
    private int id;
    private String info;

    public Demo() {
    }

    public Demo(int id, String info) {
        this.id = id;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
