package com.example.facultymanagement;

public class ResultModel {
    private String requestedFacultyName;
    private String status;
    private long timestamp;

    public ResultModel() {
    }

    public ResultModel(String requestedFacultyName, String status, long timestamp) {
        this.requestedFacultyName = requestedFacultyName;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getRequestedFacultyName() {
        return requestedFacultyName;
    }

    public String getStatus() {
        return status;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
