package com.xtuer.dto;

/**
 * 任教学科 dto.Subject:
 * subject_parentId: [{id, name}, {}]
 *
 * Created by microacup on 2016/10/17.
 */
public class Subject {
    private int id;
    private String name;

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
}
