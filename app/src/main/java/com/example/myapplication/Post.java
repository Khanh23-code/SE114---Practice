package com.example.myapplication;

public class Post {
    private final String userName;
    private final String date;
    private String content;
    private final String avatarUrl;
    private final String ownerEmail;

    public Post(String userName, String date, String content, String avatarUrl, String ownerEmail) {
        this.userName = userName;
        this.date = date;
        this.content = content;
        this.avatarUrl = avatarUrl;
        this.ownerEmail = ownerEmail;
    }

    public String getUserName() { return userName; }
    public String getDate() { return date; }
    public String getContent() { return content; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getOwnerEmail() { return ownerEmail; }

    public void setContent(String content) { this.content = content; }
}