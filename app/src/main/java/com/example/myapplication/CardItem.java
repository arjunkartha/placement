package com.example.myapplication;

public class CardItem {
    private String title;


    private String documentId;

    private String firstLetter;

    private String jtime;
    private String location;

    private String date;

    private String salary;

    public CardItem(String title,  String date, String firstLetter, String location, String jtime,  String sal, String documentId) {
        this.title = title;

        this.documentId = documentId;
        this.firstLetter = firstLetter;
        this.location = location;
        this.date = date;
        this.jtime = jtime;
        this.salary = sal;


    }

    public String getTitle() {
        return title;
    }



    public String getDocumentId(){return documentId;}
    public String getFirstLetter(){return firstLetter;}
    public String getLocation(){return location;}

    public String getDate(){return date;}


    public String getJtime(){return jtime;}
    public String getSalary(){return salary;}

}
