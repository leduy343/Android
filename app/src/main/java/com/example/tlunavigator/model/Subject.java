package com.example.tlunavigator.model;


public class Subject {

    public String id;
    public String name;
    public String code;
    public int views;
    public int credit;

    public Subject() {}

    public Subject(String id, String name, String code, int credit) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.credit = credit;
    }
}
