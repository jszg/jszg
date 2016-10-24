package com.xtuer.dto;

/**
 * TeachGrades
 *
 * Created by microacup on 2016/10/24.
 */
public class TeachGrade {
    private int id;
    private String name;
    private boolean status;
    private int adminLevel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(int adminLevel) {
        this.adminLevel = adminLevel;
    }
}
