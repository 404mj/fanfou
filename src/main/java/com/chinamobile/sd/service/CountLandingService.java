package com.chinamobile.sd.service;

import com.chinamobile.sd.commonUtils.Constant;
import com.chinamobile.sd.commonUtils.DateUtil;
import com.chinamobile.sd.dao.CountDataDao;
import com.chinamobile.sd.model.CountData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @Author: fengchen.zsx
 * @Date: 2019/12/5 15:59
 * <p>
 * redis每日排队上座数据落库定时任务
 */
@Service
public class CountLandingService {
    private final Logger logger = LogManager.getLogger(CountLandingService.class);

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private CountDataDao countDataDao;

    @Scheduled(cron = "0 0 21 * * *", zone = "Asia/Shanghai")
    public void landingValues() {
        logger.info("-------- landing start --------");
        landingTask();
        logger.info("-------- landing end --------");
    }


    public void landingTask() {
        List<CountData> cds = new ArrayList<>(360);
        for (int i = 0; i < 8; ++i) {
            List<String> tsKeys = redisTemplate.boundListOps(Constant.REDISKEY_COMPLETED_LIST).range(90 * i, (90 * (i + 1) - 1));
            List<Object> tsKeyObjs = tsKeys.stream().collect(Collectors.toList());
            List<Object> r0Wcounts = redisTemplate.opsForHash().multiGet(Constant.REDIS_R0PEOPLECOUNT_PREFIX + DateUtil.getToday(), tsKeyObjs);
            List<Object> r0Acounts = redisTemplate.opsForHash().multiGet(Constant.REDIS_R0ATTENDPROB_PREFIX + DateUtil.getToday(), tsKeyObjs);
            List<Object> r1Wcounts = redisTemplate.opsForHash().multiGet(Constant.REDIS_R1PEOPLECOUNT_PREFIX + DateUtil.getToday(), tsKeyObjs);
            List<Object> r1Acounts = redisTemplate.opsForHash().multiGet(Constant.REDIS_R1ATTENDPROB_PREFIX + DateUtil.getToday(), tsKeyObjs);

            for (int j = 0; j < 90; ++j) {
                String ts = DateUtil.parseTimestamp2String(Long.parseLong(tsKeys.get(j)));
                Object r0w = r0Wcounts.get(j);
                if (r0w == null) {
                    r0w = "0";
                }
                Object r0a = r0Acounts.get(j);
                if (r0a == null) {
                    r0a = "0";
                }
                Object r1w = r1Wcounts.get(j);
                if (r1w == null) {
                    r1w = "0";
                }
                Object r1a = r1Acounts.get(j);
                if (r1a == null) {
                    r1a = "0";
                }
                CountData cd01 = new CountData(null, 0, 1, ts, Float.parseFloat((String) r0w));
                CountData cd02 = new CountData(null, 0, 2, ts, Float.parseFloat((String) r0a));
                CountData cd11 = new CountData(null, 1, 1, ts, Float.parseFloat((String) r1w));
                CountData cd12 = new CountData(null, 1, 2, ts, Float.parseFloat((String) r1a));
                cds.add(cd01);
                cds.add(cd02);
                cds.add(cd11);
                cds.add(cd12);
            }
            countDataDao.addDatas(cds);
            cds.clear();
        }

    }
}
