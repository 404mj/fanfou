package com.chinamobile.sd.service;

import com.alibaba.fastjson.JSONObject;
import com.chinamobile.sd.commonUtils.Constant;
import com.chinamobile.sd.commonUtils.CrypUtil;
import com.chinamobile.sd.commonUtils.DateUtil;
import com.chinamobile.sd.commonUtils.RestClient4Andmu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/10/2 22:29
 * <p>
 * 和目接口异步任务
 */
@Service
public class AndmuTaskService {
    private Logger logger = LogManager.getLogger(AndmuTaskService.class);

    @Autowired
    private RestClient4Andmu restClient4Andmu;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Async("picTaskExecutor")
    public CompletableFuture<Integer> doR0QuePicJob(String timeKey) throws Exception {
        String queJson = "{\"deviceId\":\"" + Constant.R0_DEVICE_QUEUE + "\"}";
        JSONObject picJsonQue = restClient4Andmu.requestApi(Constant.PIC_REALTIME, queJson, true);
        String queurl = picJsonQue.get("data").toString();
        logger.info("r0quepic----" + queurl + " timeKey----" + timeKey);
        //存redis base64值
        String queBase = CrypUtil.encodeUrlPicToBase64(queurl);
        redisTemplate.opsForHash().put(Constant.REDIS_R0REALTIMEPIC_PREFIX + DateUtil.getToday(), timeKey, queBase);
        return CompletableFuture.completedFuture(1);
    }

    @Async("picTaskExecutor")
    public CompletableFuture<Integer> doR0AttendPicJob(String timeKey) throws Exception {
        String attJson = "{\"deviceId\":\"" + Constant.R0_DEVICE_ATTENDANCE + "\"}";
        JSONObject picJsonAtt = restClient4Andmu.requestApi(Constant.PIC_REALTIME, attJson, true);
        String atturl = picJsonAtt.get("data").toString();
        logger.info("r0attpic----" + atturl + " timeKey----" + timeKey);
        //存redis base64值
        String attBase = CrypUtil.encodeUrlPicToBase64(atturl);
        redisTemplate.opsForHash().put(Constant.REDIS_R0ATTENDANCE_PREFIX + DateUtil.getToday(), timeKey, attBase);
        return CompletableFuture.completedFuture(1);
    }

    @Async("picTaskExecutor")
    public CompletableFuture<Integer> doR1QuePicJob(String timeKey) throws Exception {
        String queJson = "{\"deviceId\":\"" + Constant.R1_DEVICE_QUEUE + "\"}";
        JSONObject picJsonQue = restClient4Andmu.requestApi(Constant.PIC_REALTIME, queJson, true);
        String queurl = picJsonQue.get("data").toString();
        logger.info("r1quepic----" + queurl + " timeKey----" + timeKey);
        //存redis base64值
        String queBase = CrypUtil.encodeUrlPicToBase64(queurl);
        redisTemplate.opsForHash().put(Constant.REDIS_R1REALTIMEPIC_PREFIX + DateUtil.getToday(), timeKey, queBase);
        return CompletableFuture.completedFuture(1);
    }

    @Async("picTaskExecutor")
    public CompletableFuture<Integer> doR1AttendPicJob(String timeKey) throws Exception {
        String attJson = "{\"deviceId\":\"" + Constant.R1_DEVICE_ATTENDANCE + "\"}";
        JSONObject picJsonAtt = restClient4Andmu.requestApi(Constant.PIC_REALTIME, attJson, true);
        String atturl = picJsonAtt.get("data").toString();
        logger.info("r1attpic----" + atturl + " timeKey----" + timeKey);
        //存redis base64值
        String attBase = CrypUtil.encodeUrlPicToBase64(atturl);
        redisTemplate.opsForHash().put(Constant.REDIS_R1ATTENDANCE_PREFIX + DateUtil.getToday(), timeKey, attBase);
        return CompletableFuture.completedFuture(1);
    }
}

