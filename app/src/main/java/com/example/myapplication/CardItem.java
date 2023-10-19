package com.example.myapplication;

public class CardItem {
    private String title;
    private String description;

    private String documentId;

    public CardItem(String title, String description, String documentId) {
        this.title = title;
        this.description = description;
        this.documentId = documentId;


    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDocumentId(){return documentId;}
}
