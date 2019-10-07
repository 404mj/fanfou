package com.chinamobile.sd.service;

import com.alibaba.fastjson.JSONObject;
import com.chinamobile.sd.commonUtils.Constant;
import com.chinamobile.sd.commonUtils.CrypUtil;
import com.chinamobile.sd.commonUtils.DateUtil;
import com.chinamobile.sd.commonUtils.RestClient4Andmu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/26 17:42
 * <p>
 * 拉取图片定时任务
 */
@Component
public class CameraAiService {
    private final Logger logger = LoggerFactory.getLogger(CameraAiService.class);
    @Autowired
    private RestClient4Andmu restClient4Andmu;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AndmuTaskService andmuTaskService;

    /**
     * 每天的早中晚饭时间开始启动，30s执行一次
     */
    @Scheduled(cron = "*/30 * 6-9,11-13,17-19  * * 1-5")
    public void executePicTask() {
        logger.info("==================" + DateUtil.date2String(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS));
    }

    /**
     * 请求和目接口得到缩略图，存到redis并通知AI service
     */
    public void syncPicSendRedisCallAiTask() {
        //同步方式取排队和上座照片
        String queJson = "{\"deviceId\":\"" + Constant.DEVICE_QUEUE + "\"}";
        String attJson = "{\"deviceId\":\"" + Constant.DEVICE_ATTENDANCE + "\"}";
        JSONObject picJsonQue = restClient4Andmu.requestApi(Constant.PIC_REALTIME, queJson, true);
        JSONObject picJsonAtt = restClient4Andmu.requestApi(Constant.PIC_REALTIME, attJson, true);
        String queurl = picJsonQue.get("data").toString();
        String atturl = picJsonAtt.get("data").toString();

        //存redis base64值
        String queBase = CrypUtil.encodeUrlPicToBase64(queurl);
        String attBase = CrypUtil.encodeUrlPicToBase64(atturl);
        //作为key的时间戳精确到秒
        String nowTime = DateUtil.getCurrentSeconds();
        redisTemplate.opsForHash().put(Constant.REDISKEY_REALTIMEPIC_PREFIX + DateUtil.getToday(), nowTime, queBase);
        redisTemplate.opsForHash().put(Constant.REDISKEY_ATTENDANCE_PREFIX + DateUtil.getToday(), nowTime, attBase);

        //通知AI service
        restClient4Andmu.notifyAiService(Constant.AISERVICEURL, "{\"time_stamp\":\"" + nowTime + "\"}");

    }

    /**
     * 请求和目接口得到缩略图，存到redis并通知AI service
     */
    public void asyncPicSendRedisCallAiTask() {
        //作为key的时间戳精确到秒
        String timeKey = DateUtil.getCurrentSeconds();
        try {
            CompletableFuture<Integer> queRes = andmuTaskService.doQuePicJob(timeKey);
            CompletableFuture<Integer> attRes = andmuTaskService.doAttendPicJob(timeKey);

            //等待
            CompletableFuture.allOf(queRes, attRes).join();

            //通知AI service
            restClient4Andmu.notifyAiService(Constant.AISERVICEURL, "{\"time_stamp\":\"" + timeKey + "\"}");
        } catch (Exception e) {
            logger.error(e.toString());
        }

    }

}
