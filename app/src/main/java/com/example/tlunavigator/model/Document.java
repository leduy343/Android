package com.example.tlunavigator.model;

public class Document {
    public String id;
    public String subjectName;
    public String subjectId;
    public String documentName;
    public String type;

    public Document() {} // Required for Firebase

    public Document(String id,String subjectId ,String subjectName, String documentName, String type) {
        this.id = id;
        this.subjectName = subjectName;
        this.subjectId = subjectId;
        this.documentName = documentName;
        this.type = type;
    }
}
