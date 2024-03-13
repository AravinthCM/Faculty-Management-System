// ResultModel.java
package com.example.facultymanagement;

public class ResultModel {
    private String requestId;
    private String requestedFacultyName;
    private String status;
    private long timestamp;

    public ResultModel() {
        // Required empty public constructor
    }

    public ResultModel(String requestId, String requestedFacultyName, String status, long timestamp) {
        this.requestId = requestId;
        this.requestedFacultyName = requestedFacultyName;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getRequestId() {
        return requestId;
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
