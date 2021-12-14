package com.law.rightive.utils;

public class EventsUtils {

    private String crn;
    private String clientName;
    private String description;

    public EventsUtils(String crn, String clientName, String description) {
        this.crn = crn;
        this.clientName = clientName;
        this.description = description;
    }

    public EventsUtils() {

    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
