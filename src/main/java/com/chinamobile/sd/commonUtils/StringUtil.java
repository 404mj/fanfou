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

    /**
     * @param str
     * @return
     */
    public static boolean isSuccess(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return str.equals(Constant.SUC);
    }


    /**
     * @param casue
     * @return
     */
    public static String parseCase(String ecause) {
        // "('[\\u4e00-\\u9fa5]+[\\- \\d+]+')"
        int i = ecause.indexOf("for key");
        if (i < 0) {
            return ecause;
        }
        return ecause.substring(0, ecause.indexOf("for key"));
    }

}
