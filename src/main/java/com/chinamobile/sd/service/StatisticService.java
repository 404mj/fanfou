package com.chinamobile.sd.service;

import com.chinamobile.sd.commonUtils.Constant;
import com.chinamobile.sd.commonUtils.DateUtil;
import com.chinamobile.sd.commonUtils.ResultUtil;
import com.chinamobile.sd.commonUtils.ServiceEnum;
import com.chinamobile.sd.model.ResultModel;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/28 21:44
 * <p>
 * 提供统计服务
 */
@Service
public class StatisticService {

    private Logger logger = LogManager.getLogger(StatisticService.class);
    private HashSet<Integer> RESTHOURFILTER = Sets.newHashSet(7, 8, 11, 12, 13, 17, 18);

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * @param restaurant
     * @return
     */
    public ResultModel getStatistic(Integer restaurant) {
        Map<String, Object> retMap = new HashMap<>();
        String queLen = "0";
        //上座率从先直出
        Double attProb = 0.0;
        //mins
        float waitTime = 0;
        Map<String, String> hisque = new LinkedHashMap<>(30);

        //过滤非饭点请求
        LocalTime now = LocalTime.now();
        if (!RESTHOURFILTER.contains(now.getHour()) || (now.isBefore(DateUtil.lunchTime) && now.isAfter(DateUtil.breakEnd))) {
            logger.info("-----------sepcial_time - " + now.toString());
            retMap.put("quelength", queLen);
            retMap.put("attendprob", attProb);
            retMap.put("waittime", waitTime);
            retMap.put("hisquecount", hisque);
            return ResultUtil.successResult(retMap);
        }

        try {
            List<String> lastKeyList = redisTemplate.opsForList().range(Constant.REDISKEY_COMPLETED_LIST, 0, 0);
            if (lastKeyList == null) {
                return ResultUtil.failResult(ServiceEnum.NO_QUERY_ERROR, "sorry~ no info yet");
            }
            String lastKey = lastKeyList.get(0);
            logger.info("-----------lastKey: " + lastKey);
            if (restaurant == 0) {//B1大餐厅
                queLen = (String) redisTemplate.opsForHash().get(Constant.REDIS_R0PEOPLECOUNT_PREFIX + DateUtil.getToday(), lastKey);
                attProb = Double.parseDouble((String) redisTemplate.opsForHash().get(Constant.REDIS_R0ATTENDPROB_PREFIX + DateUtil.getToday(), lastKey));
                waitTime = Integer.parseInt(queLen) / Constant.getPeopleFlowRate();
                hisque.put(lastKey, processQueLen(queLen, restaurant));
                processHisQue(hisque, 0);
            } else if (restaurant == 1) {//B1小餐厅
                queLen = (String) redisTemplate.opsForHash().get(Constant.REDIS_R1PEOPLECOUNT_PREFIX + DateUtil.getToday(), lastKey);
                attProb = Double.parseDouble((String) redisTemplate.opsForHash().get(Constant.REDIS_R1ATTENDPROB_PREFIX + DateUtil.getToday(), lastKey));
                waitTime = Integer.parseInt(queLen) / Constant.getPeopleFlowRate();
                hisque.put(lastKey, processQueLen(queLen, restaurant));
                processHisQue(hisque, 1);
            }


            retMap.put("quelength", processQueLen(queLen, restaurant));
            retMap.put("attendprob", attProb);
            retMap.put("waittime", waitTime);
            retMap.put("hisquecount", hisque);
            return ResultUtil.successResult(retMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        retMap.put("quelength", "0");
        retMap.put("attendprob", "0.0");
        retMap.put("waittime", "0");
        retMap.put("hisquecount", hisque);
        return ResultUtil.successResult(retMap);

    }

    /**
     * @param queLenStr
     * @return
     */
    private String processQueLen(String queLenStr, Integer restaurant) {
        Integer queLenInt = Integer.valueOf(queLenStr);
        if (restaurant == 0 && queLenInt > 1) {
            queLenInt += 8;
        }

        if (restaurant == 1 && queLenInt >= 1) {
            queLenInt += 2;
        }
        return queLenInt.toString();
    }


    /**
     * 简单取四个时间点的值，无法反映变化情况
     * @param hisque
     * @param rest
     */
    /*
    private void processHisQue(Map<String, String> hisque, Integer rest) {
        List<String> secondKeyList = redisTemplate.opsForList().range(Constant.REDISKEY_COMPLETED_LIST, 39, 39);
        List<String> thirdKeyList = redisTemplate.opsForList().range(Constant.REDISKEY_COMPLETED_LIST, 79, 79);
        List<String> fourthKeyList = redisTemplate.opsForList().range(Constant.REDISKEY_COMPLETED_LIST, 119, 119);
        String redisKey = Constant.REDIS_R0PEOPLECOUNT_PREFIX + DateUtil.getToday();

        //B1大餐厅
        if (rest == 1) {
            redisKey = Constant.REDIS_R1PEOPLECOUNT_PREFIX + DateUtil.getToday();
        }

        //递进判断
        if (secondKeyList != null && secondKeyList.size() > 0) {
            String secondKey = secondKeyList.get(0);
            logger.info("---------get queLen2  redisKey: " + redisKey + " completed_list_key: " + secondKey);
            String queLen2 = (String) redisTemplate.opsForHash().get(redisKey, secondKey);
            hisque.put(secondKey, queLen2);

            if (thirdKeyList != null && thirdKeyList.size() > 0) {
                String thirdKey = thirdKeyList.get(0);
                logger.info("---------get queLen3  redisKey: " + redisKey + " completed_list_key: " + thirdKey);
                String queLen3 = (String) redisTemplate.opsForHash().get(redisKey, thirdKey);
                hisque.put(thirdKey, queLen3);

                if (fourthKeyList != null && fourthKeyList.size() > 0) {
                    String fourthKey = fourthKeyList.get(0);
                    logger.info("--------get queLen4  redisKey: " + redisKey + " completed_list_key: " + fourthKey);
                    String queLen4 = (String) redisTemplate.opsForHash().get(redisKey, fourthKey);
                    hisque.put(fourthKey, queLen4);
                }
            }
        }
    }
    */

    /**
     * 取前半小时数据，一分钟取样
     *
     * @param hisque
     * @param rest
     */
    private void processHisQue(Map<String, String> hisque, Integer rest) {
        String redisKey = Constant.REDIS_R0PEOPLECOUNT_PREFIX + DateUtil.getToday();
        //B1大餐厅
        if (rest == 1) {
            redisKey = Constant.REDIS_R1PEOPLECOUNT_PREFIX + DateUtil.getToday();
        }

        List<String> keys = redisTemplate.opsForList().range(Constant.REDISKEY_COMPLETED_LIST, 0, 59);
        if (keys.size() <= 1) return;
        List<Object> evenKeys = IntStream.range(0, keys.size())
                .filter(i -> i % 2 == 0)
                .mapToObj(i -> keys.get(i))
                .collect(Collectors.toList());
        List<Object> counts = redisTemplate.opsForHash().multiGet(redisKey, evenKeys);

        int i = 1;
        Object v;
        for (Object k : evenKeys) {
            if (i <= counts.size() && (v = counts.get(i)) != null) {
                hisque.put(k.toString(), v.toString());
            }
        }
    }
}
