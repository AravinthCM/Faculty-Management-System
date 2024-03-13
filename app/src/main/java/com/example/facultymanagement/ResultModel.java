package com.example.facultymanagement;

public class ResultModel {
    private String requestedUserUid;
    private String status;
    private long timestamp;

    public ResultModel() {
    }

    public ResultModel(String requestedUserUid, String status, long timestamp) {
        this.requestedUserUid = requestedUserUid;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getRequestedUserUid() {
        return requestedUserUid;
    }

    public String getStatus() {
        return status;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
