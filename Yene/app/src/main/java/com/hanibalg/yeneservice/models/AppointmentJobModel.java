package com.hanibalg.yeneservice.models;

import com.google.firebase.Timestamp;

import java.sql.Time;
import java.util.Date;

public class AppointmentJobModel {
    private String docID;
    private String jobAppointedUserID;
    private String service_provider_id;
    private Timestamp date;
    private String priority;
    private String time;
    private String problem_description;
    private boolean accepted;
    private Timestamp timestamp;

    public AppointmentJobModel() {
    }

    public AppointmentJobModel(String docID,String jobAppointedUserID, String service_provider_id, Timestamp date, String priority,
                               String time, String problem_description, boolean accepted, Timestamp timestamp) {
        this.docID = docID;
        this.jobAppointedUserID = jobAppointedUserID;
        this.service_provider_id = service_provider_id;
        this.date = date;
        this.priority = priority;
        this.time = time;
        this.problem_description = problem_description;
        this.accepted = accepted;
        this.timestamp = timestamp;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getJobAppointedUserID() {
        return jobAppointedUserID;
    }

    public String getService_provider_id() {
        return service_provider_id;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getPriority() {
        return priority;
    }

    public String getTime() {
        return time;
    }

    public String getProblem_description() {
        return problem_description;
    }

    public boolean JobAccepted() {
        return accepted;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}