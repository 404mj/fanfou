package com.chinamobile.sd.commonUtils;
/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/22 0:40
 */
/**
 * 定义统一返回类
 */
public class ResultVO {
    private String code;
    private String msg;
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public void setData(Object data) {
        this.data = data;
    }

    public ResultVO(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public ResultVO(Object data) {
        this.data = data;
    }

    public static ResultVO success(Object obj) {
        return new ResultVO("0", "OK", obj);
    }

    public static ResultVO fail(Object obj) {
        return new ResultVO("1", "error", obj);
    }
}
