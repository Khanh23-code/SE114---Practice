package com.example.myapplication;

public class Post {
    private String userName;
    private String date;
    private String content;
    private String avatarUrl;

    public Post(String userName, String date, String content, String avatarUrl) {
        this.userName = userName;
        this.date = date;
        this.content = content;
        this.avatarUrl = avatarUrl;
    }

    public String getUserName() { return userName; }
    public String getDate() { return date; }
    public String getContent() { return content; }
    public String getAvatarUrl() { return avatarUrl; }
}