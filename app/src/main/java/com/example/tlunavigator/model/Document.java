package com.example.tlunavigator.model;

public class Document {
    public String id;
    public String subjectName;
    public String documentName;
    public String type;
    public String youtubeLink; // ✅ Thêm trường này nếu chưa có

    // ✅ Constructor rỗng bắt buộc cho Firebase
    public Document() {}

    // ✅ Constructor đầy đủ 5 tham số
    public Document(String id, String subjectName, String documentName, String type, String youtubeLink) {
        this.id = id;
        this.subjectName = subjectName;
        this.documentName = documentName;
        this.type = type;
        this.youtubeLink = youtubeLink;
    }
}
