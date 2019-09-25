package com.chinamobile.sd.commonUtils;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/24 14:45
 */

import org.springframework.util.StringUtils;

/**
 * 通用String工具
 */
public class StringUtil {
    public static final String EMPTYSTR = "";
    public static final String REDISKEY_TOKEN = "ANDMU_FANFOU_TOKEN";

    public static boolean isSuccess(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return str.equals("SUCCESS");
    }

}
