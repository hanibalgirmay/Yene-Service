<<<<<<< HEAD
package com.hanibalg.yeneservice.models;

import com.google.firebase.Timestamp;

public class NotificationModel {
    private String DocId;
    private String from;
    private String notificationTitle;
    private String jobID;
    private String notificationDescription;
    private boolean seen;
    private Timestamp timestamp;

    public NotificationModel() {

    }

    public NotificationModel(String DocId,String from,String notificationTitle,String jobID ,String notificationDescription,boolean seen, Timestamp timestamp) {
        this.DocId = DocId;
        this.from = from;
        this.notificationTitle = notificationTitle;
        this.notificationDescription = notificationDescription;
        this.seen = seen;
        this.jobID = jobID;
        this.timestamp = timestamp;
    }

    public String getJobID() {
        return jobID;
    }

    public boolean isSeen() {
        return seen;
    }

    public String getDocId() {
        return DocId;
    }

    public void setDocId(String docId) {
        DocId = docId;
    }

    public String getFrom() {
        return from;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public String getNotificationDescription() {
        return notificationDescription;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
=======
package com.hanibalg.yeneservice.models;

import com.google.firebase.Timestamp;

public class NotificationModel {
    private String DocId;
    private String from;
    private String notificationTitle;
    private String jobID;
    private String notificationDescription;
    private boolean seen;
    private Timestamp timestamp;

    public NotificationModel() {

    }

    public NotificationModel(String DocId,String from,String notificationTitle,String jobID ,String notificationDescription,boolean seen, Timestamp timestamp) {
        this.DocId = DocId;
        this.from = from;
        this.notificationTitle = notificationTitle;
        this.notificationDescription = notificationDescription;
        this.seen = seen;
        this.jobID = jobID;
        this.timestamp = timestamp;
    }

    public String getJobID() {
        return jobID;
    }

    public boolean isSeen() {
        return seen;
    }

    public String getDocId() {
        return DocId;
    }

    public void setDocId(String docId) {
        DocId = docId;
    }

    public String getFrom() {
        return from;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public String getNotificationDescription() {
        return notificationDescription;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
>>>>>>> 93fe2da156e9e07e2e292889abfd1898279d53b3
