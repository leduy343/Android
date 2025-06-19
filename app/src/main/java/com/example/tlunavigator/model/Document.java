package com.example.tlunavigator.model;

public class Document {
    public String id;
    public String subjectName;
    public String documentName;
    public String type;

    public Document() {} // Required for Firebase

    public Document(String id, String subjectName, String documentName, String type) {
        this.id = id;
        this.subjectName = subjectName;
        this.documentName = documentName;
        this.type = type;
    }
}
