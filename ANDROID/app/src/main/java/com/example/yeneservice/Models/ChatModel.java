package com.example.yeneservice.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class ChatModel {
    private String sender;
    private String receiver;
    private String message;
    private String image;
    private boolean isSeen;
    private Date timestamp;
    private String time;

    private String DocId;

    @ServerTimestamp
    private Date dateSent;

    public ChatModel() { }

    public ChatModel(String sender, String receiver, String message, boolean isSeen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isSeen = isSeen;
    }
    public ChatModel(String sender, String receiver, String image, boolean isSeen,Date time,String DocId) {
        this.sender = sender;
        this.receiver = receiver;
        this.image = image;
        this.isSeen = isSeen;
        this.timestamp = time;
        this.DocId = DocId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDocId() {
        return DocId;
    }

    public void setDocId(String docId) {
        DocId = docId;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }
}
