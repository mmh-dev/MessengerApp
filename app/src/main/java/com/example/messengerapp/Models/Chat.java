package com.example.messengerapp.Models;

public class Chat {

    private String messageText;

    public Chat() {
    }

    private String receiver;
    private String sender;
    private String ImageUrl;

    public Chat(String messageText, String receiver, String sender) {
        this.messageText = messageText;
        this.receiver = receiver;
        this.sender = sender;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getImageUrl() {
        return ImageUrl;
    }
}


