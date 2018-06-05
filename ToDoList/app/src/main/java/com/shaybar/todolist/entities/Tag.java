package com.shaybar.todolist.entities;

import org.chalup.microorm.annotations.Column;

import java.io.Serializable;

public class Tag implements Serializable {
    public static final String ID = "`_id`";
    public static final String TABLE_NAME = "'tag'";
    public static final String COLOR = "`color`";
    public static final String NAME = "`name`";


    @Column("_id")
    private Long id;

    @Column("name")
    private String name;

    @Column("color")
    private String color;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
