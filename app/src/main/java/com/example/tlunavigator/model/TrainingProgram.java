package com.example.tlunavigator.model;

import java.util.HashMap;
import java.util.Map;

public class TrainingProgram {
    public String id;
    public String name;
    public String code;
    public int credit;
    public Map<String, String> subjects;

    public TrainingProgram() {}

    public TrainingProgram(String id, String programName,int credit ,String code) {
        this.id = id;
        this.name = programName;
        this.code = code;
        this.credit = credit;
        this.subjects = new HashMap<>();
    }}
