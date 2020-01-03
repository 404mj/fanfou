package com.chinamobile.sd.model;

/**
 * @Author: fengchen.zsx
 * @Date: 2020/1/3 13:59
 */
public class BookedUser {
    private Integer bookUid;
    private String memId;
    private String name;
    private String msisdn;
    private String deptName;

    public BookedUser(String memId, String name, String msisdn, String deptName) {
        this.memId = memId;
        this.name = name;
        this.msisdn = msisdn;
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return "BookedUserDao{" +
                "bookedUid=" + bookUid +
                ", memId=" + memId +
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
        return memId;
    }

    public void setMemId(String memId) {
        this.memId = memId;
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
