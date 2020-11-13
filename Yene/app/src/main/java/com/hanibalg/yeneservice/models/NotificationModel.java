package com.hanibalg.yeneservice.models;

import com.google.firebase.Timestamp;

public class NotificationModel {
    private String DocId;
    private String from;
    private Boolean isAccepted;
    private String notificationTitle;
    private String notificationType;
    private String notificationDescription;
    private Timestamp timestamp;

    public NotificationModel() {

    }

    public NotificationModel(String DocId,String from,boolean isAccepted,String notificationTitle, String notificationType, String notificationDescription, Timestamp timestamp) {
        this.DocId = DocId;
        this.from = from;
        this.isAccepted = isAccepted;
        this.notificationTitle = notificationTitle;
        this.notificationType = notificationType;
        this.notificationDescription = notificationDescription;
        this.timestamp = timestamp;
    }

    public String getDocId() {
        return DocId;
    }

    public void setDocId(String docId) {
        DocId = docId;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public String getFrom() {
        return from;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public String getNotificationDescription() {
        return notificationDescription;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
