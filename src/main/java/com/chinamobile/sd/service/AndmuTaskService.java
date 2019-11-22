package com.chinamobile.sd.service;

import com.alibaba.fastjson.JSONObject;
import com.chinamobile.sd.commonUtils.Constant;
import com.chinamobile.sd.commonUtils.CrypUtil;
import com.chinamobile.sd.commonUtils.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
    private AndmuRestClientService restClient4Andmu;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private CrypUtil crypUtil;


    @Async("picTaskExecutor")
    public CompletableFuture<Integer> doR0QuePicJob(String timeKey) {
        String queJson = "{\"deviceId\":\"" + Constant.R0_DEVICE_QUEUE + "\",\"size\": \"1080x720\"}";
        try {
            JSONObject picJsonQue = restClient4Andmu.requestApi(Constant.PIC_REALTIME_NEW, queJson, true);
            String queurl = picJsonQue.getJSONObject("data").getString("url");
//            String queurl = picJsonQue.get("data").toString();
            logger.info("r0quepic----" + queurl + " timeKey----" + timeKey);
            //存redis base64值
            String queBase = crypUtil.encodeUrlPicToBase64(queurl).get(20, TimeUnit.SECONDS);
            String nowHkey = Constant.REDIS_R0REALTIMEPIC_PREFIX + DateUtil.getToday();
            redisTemplate.opsForHash().put(nowHkey, timeKey, queBase);
            redisTemplate.expire(nowHkey, Constant.REDISKEY_EXPIRES, TimeUnit.MINUTES);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            return CompletableFuture.completedFuture(1);
        }
    }

    @Async("picTaskExecutor")
    public CompletableFuture<Integer> doR0AttendPicJob(String timeKey) {
        String attJson = "{\"deviceId\":\"" + Constant.R0_DEVICE_ATTENDANCE + "\",\"size\": \"1080x720\"}";
        try {
            JSONObject picJsonAtt = restClient4Andmu.requestApi(Constant.PIC_REALTIME_NEW, attJson, true);
            String atturl = picJsonAtt.getJSONObject("data").getString("url");
            logger.info("r0attpic----" + atturl + " timeKey----" + timeKey);
            //存redis base64值
            String attBase = crypUtil.encodeUrlPicToBase64(atturl).get(20, TimeUnit.SECONDS);
            ;
            String nowHkey = Constant.REDIS_R0ATTENDANCE_PREFIX + DateUtil.getToday();
            redisTemplate.opsForHash().put(nowHkey, timeKey, attBase);
            redisTemplate.expire(nowHkey, Constant.REDISKEY_EXPIRES, TimeUnit.MINUTES);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            return CompletableFuture.completedFuture(1);
        }
    }

    @Async("picTaskExecutor")
    public CompletableFuture<Integer> doR1QuePicJob(String timeKey) throws Exception {
        String queJson = "{\"deviceId\":\"" + Constant.R1_DEVICE_QUEUE + "\",\"size\": \"1080x720\"}";
        try {
            JSONObject picJsonQue = restClient4Andmu.requestApi(Constant.PIC_REALTIME_NEW, queJson, true);
            String queurl = picJsonQue.getJSONObject("data").getString("url");
            logger.info("r1quepic----" + queurl + " timeKey----" + timeKey);
            //存redis base64值
            String queBase = crypUtil.encodeUrlPicToBase64(queurl).get(20, TimeUnit.SECONDS);
            String nowHkey = Constant.REDIS_R1REALTIMEPIC_PREFIX + DateUtil.getToday();
            redisTemplate.opsForHash().put(nowHkey, timeKey, queBase);
            redisTemplate.expire(nowHkey, Constant.REDISKEY_EXPIRES, TimeUnit.MINUTES);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            return CompletableFuture.completedFuture(1);
        }
    }

    @Async("picTaskExecutor")
    public CompletableFuture<Integer> doR1AttendPicJob(String timeKey) throws Exception {
        String attJson = "{\"deviceId\":\"" + Constant.R1_DEVICE_ATTENDANCE + "\",\"size\": \"1080x720\"}";
        try {
            JSONObject picJsonAtt = restClient4Andmu.requestApi(Constant.PIC_REALTIME_NEW, attJson, true);
            String atturl = picJsonAtt.getJSONObject("data").getString("url");
            logger.info("r1attpic----" + atturl + " timeKey----" + timeKey);
            //存redis base64值
            String attBase = crypUtil.encodeUrlPicToBase64(atturl).get(20, TimeUnit.SECONDS);
            String nowHkey = Constant.REDIS_R1ATTENDANCE_PREFIX + DateUtil.getToday();
            redisTemplate.opsForHash().put(nowHkey, timeKey, attBase);
            redisTemplate.expire(nowHkey, Constant.REDISKEY_EXPIRES, TimeUnit.MINUTES);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {//todo fix this
            return CompletableFuture.completedFuture(1);
        }
    }
}

