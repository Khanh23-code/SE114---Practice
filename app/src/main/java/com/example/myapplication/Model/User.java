package com.example.myapplication.Model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private  String name;
    private final String email;
    private  String password;
    private String phoneNumber;
    private String avatarUrl;
    private final List<String> hiddenPosts;
    private List<String> friends;
    private List<String> pendingInvitations;
    private List<String> sentRequests;

    public User(String name, String email, String password, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;

        this.hiddenPosts = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.pendingInvitations = new ArrayList<>();
        this.sentRequests = new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public List<String> getHiddenPosts() {
        return hiddenPosts;
    }

    public List<String> getFriends() {
        return friends;
    }

    public List<String> getPendingInvitations() {
        return pendingInvitations;
    }

    public List<String> getSentRequests() {
        return sentRequests;
    }
}