package com.example.tlunavigator.model;

public class Document {
    public String id;
    public String subjectName;
    public String subjectId;
    public String documentName;
    public String type;
    public String youtubeLink; 
  
    public Document() {}

    public Document(String id,String subjectId ,String subjectName, String documentName, String type, String youtubeLink) {
        this.id = id;
        this.subjectName = subjectName;
        this.subjectId = subjectId;
        this.documentName = documentName;
        this.type = type;
        this.youtubeLink = youtubeLink;
    }
}
