package com.example.facultymanagement;

import java.util.Date;

public class ApprovedModel {
    private String requestId;
    private String requestedUserUid;
    private String leaveType; // New field
    private Date startDate; // New field
    private Date endDate; // New field
    private long timestamp;

    public ApprovedModel() {
        // Required empty public constructor
    }

    public ApprovedModel(String requestId, String requestedUserUid, String leaveType, Date startDate, Date endDate, long timestamp) {
        this.requestId = requestId;
        this.requestedUserUid = requestedUserUid;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timestamp = timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getRequestedUserUid() {
        return requestedUserUid;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
