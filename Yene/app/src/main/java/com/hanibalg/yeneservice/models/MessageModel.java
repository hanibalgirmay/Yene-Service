package com.hanibalg.yeneservice.models;

import com.google.firebase.Timestamp;

public class MessageModel {
    private String sender;
    private String receiver;
    private String message;
    private Timestamp timestamp;
    private boolean isSeen;
    private String image;
    private String DocId;

    public MessageModel(){}

    public MessageModel(String sender, String receiver, String message, Timestamp timestamp, boolean isSeen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
        this.isSeen = isSeen;
    }
    public MessageModel(String sender, String receiver, String image, boolean isSeen,Timestamp timestamp,String DocId) {
        this.sender = sender;
        this.receiver = receiver;
        this.image = image;
        this.isSeen = isSeen;
        this.timestamp = timestamp;
        this.DocId = DocId;
    }

    public String getImage() {
        return image;
    }

    public String getDocId() {
        return DocId;
    }

    public void setDocId(String docId) {
        DocId = docId;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSeen() {
        return isSeen;
    }
}
