package com.chinamobile.sd.service;

import com.chinamobile.sd.commonUtils.Constant;
import com.chinamobile.sd.commonUtils.DateUtil;
import com.chinamobile.sd.commonUtils.ResultUtil;
import com.chinamobile.sd.commonUtils.ServiceEnum;
import com.chinamobile.sd.model.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/28 21:44
 * <p>
 * 提供统计服务
 */
@Service
public class StatisticService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * @param restaurant
     * @return
     */
    public ResultModel getStatistic(Integer restaurant) {
        Map<String, Object> retMap = new HashMap<>();
        //排队人数
        String queLen = null;
        String attend = null;
        //上座率
        Double attProb = null;
        float waitTime = 0;
        Map<String, String> hisque = new LinkedHashMap<>();


        List<String> lastKeyList = redisTemplate.opsForList().range(Constant.REDISKEY_COMPLETED_LIST, 0, 0);
        //还没有数据如何
        if (lastKeyList == null || lastKeyList.size() == 0) {
            return ResultUtil.failResult(ServiceEnum.NO_QUERY_ERROR, "sorry~ no info yet");
        }
        String lastKey = lastKeyList.get(0);

        if (restaurant == 0) {//B1大餐厅
            queLen = redisTemplate.opsForHash().get(Constant.REDIS_R0PEOPLECOUNT_PREFIX + DateUtil.getToday(), lastKey).toString();
//            attend = redisTemplate.opsForHash().get(Constant.REDIS_R0ATTENDPROB_PREFIX + DateUtil.getToday(), lastKey).toString();
//            attProb = Double.valueOf(attend) / Constant.R0_FULLSEAT_PEOPLE;
            //上座率改为从先直出
            attProb = (Double) redisTemplate.opsForHash().get(Constant.REDIS_R0ATTENDPROB_PREFIX + DateUtil.getToday(), lastKey);
            waitTime = Integer.parseInt(queLen) / Constant.getPeopleFlowRate();
            //todo 前1小时,15分钟段,四个数据

        } else if (restaurant == 1) {//B1小餐厅
            queLen = redisTemplate.opsForHash().get(Constant.REDIS_R1PEOPLECOUNT_PREFIX + DateUtil.getToday(), lastKey).toString();
//            attend = redisTemplate.opsForHash().get(Constant.REDIS_R1ATTENDPROB_PREFIX + DateUtil.getToday(), lastKey).toString();
//            attProb = Double.valueOf(attend) / Constant.R0_FULLSEAT_PEOPLE;
            attProb = (Double) redisTemplate.opsForHash().get(Constant.REDIS_R1ATTENDPROB_PREFIX + DateUtil.getToday(), lastKey);
            waitTime = Integer.parseInt(queLen) / Constant.getPeopleFlowRate();
            //todo: 获取历史排队人数
        }
        retMap.put("quelength", processQueLen(queLen));
        retMap.put("attendprob", attProb.toString());
        retMap.put("waittime", waitTime);
        retMap.put("hisquecount", hisque);

        return ResultUtil.successResult(retMap);
    }

    /**
     * @param queLenStr
     * @return
     */
    private Integer processQueLen(String queLenStr) {
        Integer queLenInt = Integer.valueOf(queLenStr);
        if (queLenInt > 1) {
            queLenInt += 8;
        }
        return queLenInt;
    }
}
