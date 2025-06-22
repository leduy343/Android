package com.example.tlunavigator.model;

public class Document {
    public String id;
    public String subjectName;
    public String subjectId;
    public String documentName;
    public String type;
    public String youtubeLink;

    public Document() {}

    public Document(String id, String subjectId, String subjectName, String documentName, String type, String youtubeLink) {
        this.id = id;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.documentName = documentName;
        this.type = type;
        this.youtubeLink = youtubeLink;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getSubject() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getDocumentName() {
        return documentName;
    }

    public String getType() {
        return type;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }
}
