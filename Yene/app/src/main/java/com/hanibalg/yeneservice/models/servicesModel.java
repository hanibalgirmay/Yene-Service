package com.hanibalg.yeneservice.models;

import com.google.firebase.Timestamp;

public class servicesModel {
    String serviceId;
    String service_name;
    String service_icon;
    String img;
    Timestamp timestamp;

    public servicesModel() {
    }

    public servicesModel(String serviceId, String service_name, String service_icon,String img) {
        this.serviceId = serviceId;
        this.service_name = service_name;
        this.service_icon = service_icon;
        this.img = img;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getService_name() {
        return service_name;
    }

    public String getService_icon() {
        return service_icon;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getImg() {
        return img;
    }
}
