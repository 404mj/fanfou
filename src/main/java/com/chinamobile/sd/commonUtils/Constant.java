package com.chinamobile.sd.commonUtils;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/26 12:53
 */
public class Constant {
    public static final String EMPTYSTR = "";
    public static final String SUC = "SUCCESS";

    /**
     * redis key constant
     */
    public static final String REDISKEY_TOKEN = "ANDMU_FANFOU_TOKEN";
    // 一个List作为队列，lpush，rpop(改为调用接口传参)，一个List作为lpush作为最新的数据，取则lrang
    public static final String REDISKEY_COMPLETED_LIST = "COMPLETED_LIST";
    //两个hash，一个以每天为单位存储时间戳和图片，一个以天为单位存储时间戳和图片识别结果
    public static final String REDISKEY_REALTIMEPIC_PREFIX = "REALTIMEPIC_";
    public static final String REDISKEY_PEOPLECOUNT_PREFIX = "PEOPLECOUNT_";
    //上座率
    public static final String REDISKEY_ATTENDANCE_PREFIX = "ATTENDANCE_";
    public static final String REDISKEY_ATTENDANCEPROB_PREFIX = "ATTENDANCEPROB_";


    /**
     * api list
     */
    public static final String TOKEN_POST = "https://open.andmu.cn/v3/open/api/token";
    public static final String DEVICELIST_POST = "https://open.andmu.cn/v3/open/api/pro/device/list";
    public static final String VIDEO_PLAY = "https://open.andmu.cn/v3/open/api/websdk/live";
    public static final String PIC_REALTIME = "https://open.andmu.cn/v3/open/api/pro/camera/thumbnail/realtime";
    //AI service
    public static final String AISERVICEURL = "";

    /**
     * device list
     * 做了请求接口，暂时固定到代码中
     */
    public static final String DEVICE_QUEUE = "";
    public static final String DEVICE_ATTENDANCE = "";

}
