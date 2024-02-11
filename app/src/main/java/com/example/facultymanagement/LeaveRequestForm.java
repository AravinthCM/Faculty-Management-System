package com.example.facultymanagement;

public class LeaveRequestForm {

    String facname,leavetype,leavereason,startdate,enddate,status;

    public LeaveRequestForm() {
    }

    public LeaveRequestForm(String facname,String leavetype ,String leavereason, String startdate, String enddate, String status) {
        this.facname = facname;
        this.leavetype = leavetype;
        this.leavereason = leavereason;
        this.startdate = startdate;
        this.enddate = enddate;
        this.status=status;
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

    public void setLeavetype(String leavereason) {
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
        startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        enddate = enddate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
