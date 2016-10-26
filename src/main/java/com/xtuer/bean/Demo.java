package com.xtuer.bean;

import org.hibernate.validator.constraints.NotBlank;

public class Demo {
    private int id;

    @NotBlank(message="信息不能为空")
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
