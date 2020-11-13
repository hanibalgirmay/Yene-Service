package com.hanibalg.yeneservice.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class JobListPublic {
    private String JobId;
    private String serviceName;
    private String service_type;
    private String provider_type;
    private String problem_desc;
    private Boolean onlyProfessional;
    private String jobDate;
    private Timestamp timestamp;
    private String userID;
    private GeoPoint location;

    public JobListPublic() {
    }

    public JobListPublic(String jobId, String serviceName, String service_type, String provider_type, String problem_desc, Boolean onlyProfessional, String jobDate, Timestamp timestamp, String userID,GeoPoint location) {
        JobId = jobId;
        this.serviceName = serviceName;
        this.service_type = service_type;
        this.provider_type = provider_type;
        this.problem_desc = problem_desc;
        this.onlyProfessional = onlyProfessional;
        this.jobDate = jobDate;
        this.timestamp = timestamp;
        this.userID = userID;
        this.location = location;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public void setJobId(String jobId) {
        JobId = jobId;
    }

    public String getJobId() {
        return JobId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getService_type() {
        return service_type;
    }

    public String getProvider_type() {
        return provider_type;
    }

    public String getProblem_desc() {
        return problem_desc;
    }

    public Boolean getOnlyProfessional() {
        return onlyProfessional;
    }

    public String getJobDate() {
        return jobDate;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getUserID() {
        return userID;
    }
}
