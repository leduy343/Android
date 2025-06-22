package com.example.tlunavigator.model;

public class StudyProgressItem {
    private String subjectName;
    private int credit;

    public StudyProgressItem(String subjectName, int credit) {
        this.subjectName = subjectName;
        this.credit = credit;
    }

    public String getSubjectName() { return subjectName; }
    public int getCredit() { return credit; }
}
