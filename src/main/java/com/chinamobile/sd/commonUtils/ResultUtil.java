package com.chinamobile.sd.commonUtils;
/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/22 0:40
 */

import com.chinamobile.sd.model.ResultModel;

import java.io.Serializable;

/**
 * 定义统一返回类
 * status:
 */
public class ResultUtil implements Serializable {


    /**
     * 构造处理结果
     *
     * @param result  结果
     * @param model   数据类型
     * @param success 操作状态
     * @param code    结果代码
     * @param msg     结果说明
     * @return Result<T>
     */
    public static <T> ResultModel<T> customResult(ResultModel<T> result, T model, boolean success, String code, String msg) {
        result.setStatus(code);
        result.setMsg(msg);
        result.setSuccess(success);
        if (model != null) {
            result.setData(model);
        }
        return result;
    }

    /**
     * 根据状态枚举和数据构造处理结果
     *
     * @param serviceEnum
     * @param model
     * @param <T>
     * @return
     */
    public static <T> ResultModel<T> customResult(ServiceEnum serviceEnum, T model) {
        ResultModel<T> result = new ResultModel<>();
        result.setSuccess(StringUtil.isSuccess(serviceEnum.getCode()));
        result.setStatus(serviceEnum.getCode());
        result.setMsg(serviceEnum.getValue());
        result.setData(model);
        return result;
    }


    /**
     * 构造失败结果
     *
     * @param serviceEnum
     * @param info
     * @return
     */
    public static ResultModel failResult(ServiceEnum serviceEnum, String info) {
        ResultModel result = new ResultModel();
        result.setSuccess(false);
        result.setStatus(serviceEnum.getCode());
        result.setMsg(serviceEnum.getValue() + info);
        return result;
    }

    /**
     * 构造失败结果
     *
     * @param serviceEnum
     * @param info
     * @return
     */
    public static ResultModel failResult(ServiceEnum serviceEnum, String info, String data) {
        ResultModel result = new ResultModel();
        result.setSuccess(false);
        result.setStatus(serviceEnum.getCode());
        result.setMsg(serviceEnum.getValue() + info);
        result.setData(data);
        return result;
    }

    /**
     * 构造成功结果
     *
     * @param obj
     * @return
     */
    public static <T> ResultModel<T> successResult(T obj) {
        return customResult(ServiceEnum.SUCCESS, obj);
    }
}
