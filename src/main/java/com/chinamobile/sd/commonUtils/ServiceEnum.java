package com.chinamobile.sd.commonUtils;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/25 9:37
 */
public enum ServiceEnum {

    SUCCESS("SUCCESS", "调用成功"),
    PARAM_FORMAT_ERROR("FAIL_BIZ_PARAM_ERROR", "输入的参数有误"),
    VALIDATE_ERROR("FAIL_BIZ_VALIDATE_ERROR", "数据检验异常"),
    INPUT_NULL("FAIL_BIZ_INPUT_NULL", "必要参数不能为空，请填写"),
    OTHER_ERROR("FAIL_BIZ_OTHER_ERROR", "其他错误"),
    NO_QUERY_INFO("FAIL_BIZ_NO_QUERY_INFO", "查询信息不存在"),
    DELETE_ERROR("FAIL_BIZ_DELETE_ERROR", "信息删除失败"),
    SAVE_ERROR("SAVE_ERROR", "信息添加失败"),
    UPDATE_ERROR("UPDATE_ERROR", "修改信息失败"),
    COMMIT_REPEAT_ERROR("FAIL_BIZ_COMMIT_REPEAT_ERROR", "不能重复提交"),
    NO_QUERY_ERROR("FAIL_BIZ_NO_QUERY_ERROR", "您请求的信息不存在。"),
    SYSTEM_ERROR("FAIL_BIZ_SYSTEM_ERROR", "系统开小差啦~请稍后再试");


    private String code;
    private String value;

    ServiceEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static ServiceEnum getEnum(String code) {
        for (ServiceEnum serviceEnum : ServiceEnum.values()) {
            if (serviceEnum.getCode().equals(code)) {
                return serviceEnum;
            }
        }
        return null;
    }

}
