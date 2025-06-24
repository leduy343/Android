package com.example.tlunavigator.model;

public class Course {
    private String id;
    private String name;

    public Course(String code, String name) {
        this.id = code;
        this.name = name;
    }

    public String getCode() { return id; }
    public String getName() { return name; }
}
