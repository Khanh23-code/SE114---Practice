package com.example.myapplication.Model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String email;
    private String password;
    private List<String> hiddenPosts;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.hiddenPosts = new ArrayList<>();
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public List<String> getHiddenPosts() {
        return hiddenPosts;
    }
}