package com.example.messengerapp.Models;

public class Chat {

    private String message;
    private String receiver;
    private String sender;

    public Chat() {
    }

    public Chat(String messageText, String receiver, String sender) {
        this.message = messageText;
        this.receiver = receiver;
        this.sender = sender;
    }

    public String getMessage() {
            return message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

}


