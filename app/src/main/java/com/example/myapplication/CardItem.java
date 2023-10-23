package com.example.myapplication;

public class CardItem {
    private String title;
    private String description;

    private String documentId;

    private String firstLetter;
    public CardItem(String title, String description, String documentId, String firstLetter) {
        this.title = title;
        this.description = description;
        this.documentId = documentId;
        this.firstLetter = firstLetter;

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDocumentId(){return documentId;}
    public String getFirstLetter(){return firstLetter;}
}
