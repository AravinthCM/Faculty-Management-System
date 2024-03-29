package com.example.facultymanagement;

public class UserList {

    private String facname,leavetype,leavereason,startdate,enddate,status,userUid;

    public UserList() {

    }

    public UserList(String facname,String leavetype, String leavereason, String startdate, String enddate, String status,String userUid) {
        this.facname = facname;
        this.leavetype = leavetype;
        this.leavereason = leavereason;
        this.startdate = startdate;
        this.enddate = enddate;
        this.status = status;
        this.userUid=userUid;
    }

    public String getFacname() {
        return facname;
    }

    public void setFacname(String facname) {
        this.facname = facname;
    }

    public String getLeaveType() {
        return leavetype;
    }

    public void setLeaveType(String leaveType) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
