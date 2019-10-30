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

import java.util.*;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/28 21:44
 * <p>
 * 提供统计服务
 */
@Service
public class StatisticService {

    private Logger logger = LogManager.getLogger(StatisticService.class);
    private HashSet<String> RESTHOURFILTER = Sets.newHashSet("06", "07", "08", "11", "12",
            "13", "17", "18", "19");

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * @param restaurant
     * @return
     */
    public ResultModel getStatistic(Integer restaurant) {
        Map<String, Object> retMap = new HashMap<>();
        String queLen = "0";
        //String attend = null;
        Double attProb = 0.0;
        //mins
        float waitTime = 0;
        Map<String, String> hisque = new LinkedHashMap<>(16);

        //过滤饭点请求
        String nowHour = DateUtil.getCurrentHour();
        if (!RESTHOURFILTER.contains(nowHour)) {
            logger.info("-----------not_eatting_time - " + nowHour);
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
                //attend = redisTemplate.opsForHash().get(Constant.REDIS_R0ATTENDPROB_PREFIX + DateUtil.getToday(), lastKey).toString();
                //attProb = Double.valueOf(attend) / Constant.R0_FULLSEAT_PEOPLE;
                //上座率改为从先直出
                attProb = Double.parseDouble((String) redisTemplate.opsForHash().get(Constant.REDIS_R0ATTENDPROB_PREFIX + DateUtil.getToday(), lastKey));
                waitTime = Integer.parseInt(queLen) / Constant.getPeopleFlowRate();
                //前1小时,15分钟段,四个数据
                hisque.put(lastKey, processQueLen(queLen));
                processHisQue(hisque, 0);
            } else if (restaurant == 1) {//B1小餐厅
                queLen = (String) redisTemplate.opsForHash().get(Constant.REDIS_R1PEOPLECOUNT_PREFIX + DateUtil.getToday(), lastKey);
                //attend = redisTemplate.opsForHash().get(Constant.REDIS_R1ATTENDPROB_PREFIX + DateUtil.getToday(), lastKey).toString();
                //attProb = Double.valueOf(attend) / Constant.R0_FULLSEAT_PEOPLE;
                attProb = Double.parseDouble((String) redisTemplate.opsForHash().get(Constant.REDIS_R1ATTENDPROB_PREFIX + DateUtil.getToday(), lastKey));
                waitTime = Integer.parseInt(queLen) / Constant.getPeopleFlowRate();
                hisque.put(lastKey, queLen);
                processHisQue(hisque, 1);
            }


            retMap.put("quelength", processQueLen(queLen));
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
    private String processQueLen(String queLenStr) {
        Integer queLenInt = Integer.valueOf(queLenStr);
        if (queLenInt >= 1) {
            queLenInt += 8;
        }
        return queLenInt.toString();
    }


    /**
     * @param hisque
     * @param rest
     */
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
}
