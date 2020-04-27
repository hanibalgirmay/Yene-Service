package com.example.yeneservice.Models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

public class NotificationModel {


    private String from,messages,to;
    private Timestamp timestamp;

    @DocumentId
    private String DocID;

    public NotificationModel(){

    }

    public NotificationModel(String from, String messages,String to, Timestamp timestamp, String DocID) {
        this.from = from;
        this.messages = messages;
        this.to = to;
        this.timestamp = timestamp;
        this.DocID = DocID;
    }


    public String getDocID() {
        return DocID;
    }

    public void setDocID(String docID) {
        DocID = docID;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String message) {
        this.messages = message;
    }
}
