package com.example.facultymanagement;

import java.util.Date;

public class ApprovedModel {
    private String facname;
    private String leavereason;
    private String leavetype;
    private String startdate;
    private String enddate;
    private String status;
    private String subjectclasshour;
    private String submissiondate;
    private String substitutionstaff;
    private long timestamp;
    private String userUid;

    public ApprovedModel() {
    }

    public ApprovedModel(String facname, String leavereason, String leavetype, String startdate,
                         String enddate, String status, String subjectclasshour, String submissiondate,
                         String substitutionstaff, long timestamp, String userUid) {
        this.facname = facname;
        this.leavereason = leavereason;
        this.leavetype = leavetype;
        this.startdate = startdate;
        this.enddate = enddate;
        this.status = status;
        this.subjectclasshour = subjectclasshour;
        this.submissiondate = submissiondate;
        this.substitutionstaff = substitutionstaff;
        this.timestamp = timestamp;
        this.userUid = userUid;
    }

    public String getFacname() {
        return facname;
    }

    public String getLeavereason() {
        return leavereason;
    }

    public String getLeavetype() {
        return leavetype;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getStatus() {
        return status;
    }

    public String getSubjectclasshour() {
        return subjectclasshour;
    }

    public String getSubmissiondate() {
        return submissiondate;
    }

    public String getSubstitutionstaff() {
        return substitutionstaff;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUserUid() {
        return userUid;
    }
}
