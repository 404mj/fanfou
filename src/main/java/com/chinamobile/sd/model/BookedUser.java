package com.chinamobile.sd.model;

/**
 * @Author: fengchen.zsx
 * @Date: 2020/1/3 13:59
 */
public class BookedUser {
    private Integer bookUid;
    private String memid;
    private String name;
    private String msisdn;
    private String deptName;

    public BookedUser(Integer bookUid, String memId, String name, String msisdn, String deptName) {
        this.bookUid = bookUid;
        this.memid = memId;
        this.name = name;
        this.msisdn = msisdn;
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return "BookedUserDao{" +
                "bookedUid=" + bookUid +
                ", memId=" + memid +
                ", name='" + name + '\'' +
                ", msisdn='" + msisdn + '\'' +
                ", deptName='" + deptName + '\'' +
                '}';
    }

    public Integer getBookUid() {
        return bookUid;
    }

    public void setBookUid(Integer bookUid) {
        this.bookUid = bookUid;
    }

    public String getMemId() {
        return memid;
    }

    public void setMemId(String memId) {
        this.memid = memId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
