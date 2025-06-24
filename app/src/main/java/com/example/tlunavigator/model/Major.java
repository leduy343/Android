package com.example.tlunavigator.model;

public class Major {
    public String id;
    public String name;
    public String code;
    public  Major(){}
    public Major(String id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
    public String getId() { return id; }

    public String getCode() { return code; }
    public String getName() { return name; }
}
