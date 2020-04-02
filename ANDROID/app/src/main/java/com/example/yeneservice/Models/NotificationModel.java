package com.example.yeneservice.Models;

public class NotificationModel {


    private String from,messages;

    public NotificationModel(){

    }

    public NotificationModel(String from, String messages) {
        this.from = from;
        this.messages = messages;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String message) {
        this.messages = message;
    }
}
