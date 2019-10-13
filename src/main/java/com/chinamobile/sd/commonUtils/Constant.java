package com.chinamobile.sd.commonUtils;

import java.util.HashMap;
import java.util.Random;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/26 12:53
 */
public class Constant {

    /**
     * String constant
     */
    public static final String EMPTYSTR = "";
    public static final String SUC = "SUCCESS";
    public static final String RECIPE_SEPRATE = "\\|";
    /**
     * number costant
     */
    //主要餐桌位总承载人数
    public static Integer R0_FULLSEAT_PEOPLE = 256;
    public static Integer R1_FULLSEAT_PEOPLE = 256;

    //计算排队通过速率
    public static float getPeopleFlowRate() {
        int i = (int) ((Math.random() * 4) + 1);
        return (float) (6 + (i / 10.0));
    }

    /**
     * redis key constant
     */
    public static final String REDISKEY_TOKEN = "ANDMU_FANFOU_TOKEN";
    public static final String REDISKEY_COMPLETED_LIST = "COMPLETED_LIST";
    public static final String REDIS_R0REALTIMEPIC_PREFIX = "R0_WAITPIC_";
    public static final String REDIS_R0PEOPLECOUNT_PREFIX = "R0_WAITCOUNT_";
    //B1小餐厅
    public static final String REDIS_R1REALTIMEPIC_PREFIX = "R1_WAITPIC_";
    public static final String REDIS_R1PEOPLECOUNT_PREFIX = "R1_WAITCOUNT_";
    //B1大餐厅上座率
    public static final String REDIS_R0ATTENDANCE_PREFIX = "R0_ATTENDPIC_";
    public static final String REDIS_R0ATTENDPROB_PREFIX = "R0_ATTENDCOUNT_";
    //B1小餐厅上座率
    public static final String REDIS_R1ATTENDANCE_PREFIX = "R1_ATTENDPIC_";
    public static final String REDIS_R1ATTENDPROB_PREFIX = "R1_ATTENDCOUNT_";
    //餐厅前缀
    public static final String B1_RESTAURANT_PREFIX0 = "R0";
    public static final String B1_RESTAURANT_PREFIX1 = "R1";


    /**
     * api list
     */
    public static final String TOKEN_POST = "https://open.andmu.cn/v3/open/api/token";
    public static final String DEVICELIST_POST = "https://open.andmu.cn/v3/open/api/pro/device/list";
    public static final String VIDEO_PLAY = "https://open.andmu.cn/v3/open/api/websdk/live";
    public static final String PIC_REALTIME = "https://open.andmu.cn/v3/open/api/pro/camera/thumbnail/realtime";
    //AI service
    public static final String AISERVICEURL = "http://172.20.10.14:8080/tee_egg";

    /**
     * device list
     * 做了请求接口，暂时固定到代码中
     */
    //B1大餐厅设备
    public static final String R0_DEVICE_QUEUE = "xxxxS_001231404901";
    public static final String R0_DEVICE_ATTENDANCE = "xxxxS_0012313a1fc1";
    //B1小餐厅设备
    public static final String R1_DEVICE_QUEUE = "";
    public static final String R1_DEVICE_ATTENDANCE = "";


}
