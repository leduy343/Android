package com.example.tlunavigator.model;

public class Document {
    public String id;
    public String subjectName;    // Tên môn học
    public String documentName;   // Tên tài liệu
    public String type;           // Loại

    public Document() {}

    public Document(String id, String subjectName, String documentName, String type) {
        this.id = id;
        this.subjectName = subjectName;
        this.documentName = documentName;
        this.type = type;
    }
}
