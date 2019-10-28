package com.chinamobile.sd.commonUtils;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/10/28 9:57
 */
public class IntegerUtil {

    public static boolean wrongInteger(Integer number, Integer flag) {
        if (number != null && number > -1 && number <= flag) {
            return false;
        }
        return true;
    }

}
