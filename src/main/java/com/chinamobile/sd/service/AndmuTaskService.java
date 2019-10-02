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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/10/2 22:29
 * <p>
 * 和目接口异步任务
 */
@Service
public class AndmuTaskService {
    private Logger logger = LoggerFactory.getLogger(AndmuTaskService.class);

    @Autowired
    private RestClient4Andmu restClient4Andmu;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Async("picTaskExecutor")
    public Future<Integer> doQuePicJob(String timeKey) throws Exception {
        String queJson = "{\"deviceId\":\"" + Constant.DEVICE_QUEUE + "\"}";
        JSONObject picJsonQue = restClient4Andmu.requestApi(Constant.PIC_REALTIME, queJson, true);
        String queurl = picJsonQue.get("data").toString();
        //存redis base64值
        String queBase = CrypUtil.encodeUrlPicToBase64(queurl);
        redisTemplate.opsForHash().put(Constant.REDISKEY_REALTIMEPIC_PREFIX + DateUtil.getToday(), timeKey, queBase);
        return new AsyncResult<>(1);
    }

    @Async("picTaskExecutor")
    public Future<Integer> doAttendPicJob(String timeKey) throws Exception {
        String attJson = "{\"deviceId\":\"" + Constant.DEVICE_ATTENDANCE + "\"}";
        JSONObject picJsonAtt = restClient4Andmu.requestApi(Constant.PIC_REALTIME, attJson, true);
        String atturl = picJsonAtt.get("data").toString();
        //存redis base64值
        String attBase = CrypUtil.encodeUrlPicToBase64(atturl);
        redisTemplate.opsForHash().put(Constant.REDISKEY_ATTENDANCE_PREFIX + DateUtil.getToday(), timeKey, attBase);
        return new AsyncResult<>(1);
    }
}

