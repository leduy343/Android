package com.example.tlunavigator.model;

import java.util.HashMap;
import java.util.Map;

public class TrainingProgram {
    public String id;
    public String programName;
    public String description;
    public int credit;
    public Map<String, Boolean> subjects;

    public TrainingProgram() {}

    public TrainingProgram(String id, String programName,int credit ,String description) {
        this.id = id;
        this.programName = programName;
        this.description = description;
        this.credit = credit;
        this.subjects = new HashMap<>();
    }}
