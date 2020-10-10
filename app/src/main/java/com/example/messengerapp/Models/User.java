package com.example.messengerapp.Models;

public class User {

    private String id;
    private String email;
    private String username;
    private String ImageUrl;
    private boolean hasChat;

    public User(String id, String email, String username, String imageUrl) {
        this.id = id;
        this.email = email;
        this.username = username;
        ImageUrl = imageUrl;
    }

    public User() {
    }

    public boolean isHasChat() {
        return hasChat;
    }

    public void setHasChat(boolean hasChat) {
        this.hasChat = hasChat;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
