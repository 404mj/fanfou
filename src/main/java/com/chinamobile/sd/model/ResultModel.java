package com.chinamobile.sd.model;

import java.io.Serializable;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/25 14:01
 */
public class ResultModel<T> implements Serializable {
    private Boolean success;
    private String status;
    private String msg;
    private T data;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResultModel() {
    }

    public ResultModel(Boolean success, String status, String msg, T data) {
        this.success = success;
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
}
