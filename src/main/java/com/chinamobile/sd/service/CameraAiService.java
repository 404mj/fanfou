package com.chinamobile.sd.service;

import com.chinamobile.sd.commonUtils.Constant;
import com.chinamobile.sd.commonUtils.DateUtil;
import com.chinamobile.sd.commonUtils.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/26 17:42
 * <p>
 * 拉取图片定时任务
 */
@Component
public class CameraAiService {
    private final Logger logger = LogManager.getLogger(CameraAiService.class);
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private AndmuTaskService andmuTaskService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 每天的早中晚饭时间开始启动，30s执行一次
     * 秒 分 时 每月第几天 月 星期 年
     */
    @Scheduled(cron = "*/30 * 6-8,11-13,17-19 * * *", zone = "Asia/Shanghai")
    public void executePicTask() {
        logger.info("currentthread: {} - crontaskexec: {}", Thread.currentThread().getName(),
                DateUtil.date2String(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS));

        //推送移动社区
        String pushValue = redisTemplate.opsForValue().get(Constant.REDIS_MOBILE_PUSHFLAG);
        if (StringUtils.isEmpty(pushValue)) {
            redisTemplate.opsForValue().set(Constant.REDIS_MOBILE_PUSHFLAG, Constant.REDIS_MOBILE_PUSHVALUE,
                    Constant.PUSHFLAG_EXPIRES, TimeUnit.MINUTES);
            notifyService.notifyMobile();
        }

        asyncPicSendRedisCallAiTask();
    }

    /**
     * 同步方式请求和目接口得到缩略图，存到redis并通知AI service
     */

    /**
     * 请求和目接口得到缩略图，存到redis并通知AI service
     */
    public void asyncPicSendRedisCallAiTask() {
        //作为key的时间戳精确到秒
        String timeKey = DateUtil.getCurrentSeconds();
        try {
            CompletableFuture<Integer> r0QueRes = andmuTaskService.doR0QuePicJob(timeKey);
            CompletableFuture<Integer> r0AttRes = andmuTaskService.doR0AttendPicJob(timeKey);
            CompletableFuture<Integer> r1QueRes = andmuTaskService.doR1QuePicJob(timeKey);
            CompletableFuture<Integer> r1AttRes = andmuTaskService.doR1AttendPicJob(timeKey);

            //等待
            CompletableFuture.allOf(r0QueRes, r0AttRes, r1QueRes, r1AttRes).join();

            //通知AI service
//            notifyService.notifyAiService(Constant.AISERVICEURL, "{\"time_stamp\":\"" + timeKey + "\"}");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }
}
