package com.example.facultymanagement;

public class ApprovedModel {
    private String requestId;
    private String requestedFacultyName;
    private long timestamp;

    public ApprovedModel() {
        // Required empty public constructor
    }

    public ApprovedModel(String requestId, String requestedFacultyName, long timestamp) {
        this.requestId = requestId;
        this.requestedFacultyName = requestedFacultyName;
        this.timestamp = timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getRequestedFacultyName() {
        return requestedFacultyName;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

