package com.example.facultymanagement;

public class CasualLeaveFormModel {

    private String facname, leavetype, leavereason, startdate, enddate, substitutionstaff, subjectclasshour, status, submissiondate;
    private String userUid;

    public CasualLeaveFormModel() {
    }

    public CasualLeaveFormModel(String facname, String leavetype, String leavereason, String startdate, String enddate, String substitutionstaff, String subjectclasshour, String status, String submissiondate, String userUid) {
        this.facname = facname;
        this.leavetype = leavetype;
        this.leavereason = leavereason;
        this.startdate = startdate;
        this.enddate = enddate;
        this.substitutionstaff = substitutionstaff;
        this.subjectclasshour = subjectclasshour;
        this.status = status;
        this.submissiondate = submissiondate;
        this.userUid = userUid;
    }

    public String getFacname() {
        return facname;
    }

    public void setFacname(String facname) {
        this.facname = facname;
    }

    public String getLeavetype() {
        return leavetype;
    }

    public void setLeavetype(String leavetype) {
        this.leavetype = leavetype;
    }

    public String getLeavereason() {
        return leavereason;
    }

    public void setLeavereason(String leavereason) {
        this.leavereason = leavereason;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getSubstitutionstaff() {
        return substitutionstaff;
    }

    public void setSubstitutionstaff(String substitutionstaff) {
        this.substitutionstaff = substitutionstaff;
    }

    public String getSubjectclasshour() {
        return subjectclasshour;
    }

    public void setSubjectclasshour(String subjectclasshour) {
        this.subjectclasshour = subjectclasshour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubmissiondate() {
        return submissiondate;
    }

    public void setSubmissiondate(String submissiondate) {
        this.submissiondate = submissiondate;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
