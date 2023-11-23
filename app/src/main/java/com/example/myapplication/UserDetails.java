package com.example.myapplication;


public class UserDetails {
    private String userName;
    private String userEmail;
    private String userId;

    public UserDetails(String userName, String userEmail, String userId) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserId() {
        return userId;
    }
}