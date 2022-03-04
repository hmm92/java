package com.hakime.sparkjava.core;

public enum StatusResponse {
    SUCCESS("Success"),
    ERROR("Error");

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    StatusResponse(String success) {
    }
    // constructors, getters
}
