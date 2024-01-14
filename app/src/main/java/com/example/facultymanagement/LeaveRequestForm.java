package com.example.facultymanagement;

public class LeaveRequestForm {

    String facname,leavetype,leavereason,Startdate,Enddate;

    public LeaveRequestForm() {
    }

    public LeaveRequestForm(String facname,String leavetype, String leavereason, String startdate, String enddate) {
        this.facname = facname;
        this.leavereason=leavereason;
        this.leavereason = leavereason;
        Startdate = startdate;
        Enddate = enddate;
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
        return Startdate;
    }

    public void setStartdate(String startdate) {
        Startdate = startdate;
    }

    public String getEnddate() {
        return Enddate;
    }

    public void setEnddate(String enddate) {
        Enddate = enddate;
    }
}
