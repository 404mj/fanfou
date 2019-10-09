package com.chinamobile.sd.service;

import com.chinamobile.sd.commonUtils.Constant;
import com.chinamobile.sd.commonUtils.DateUtil;
import com.chinamobile.sd.commonUtils.ResultUtil;
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

    public ResultModel getStatistic() {

        Map<String, Object> retMap = new HashMap<>();

        //取最新排队人数和上座人数
        List<String> lastKeyList = redisTemplate.opsForList().range(Constant.REDISKEY_COMPLETED_LIST, 0, 0);
        String lastKey = lastKeyList.get(0);
        //todo:: 如果还没有数据如何处理？？？
        String queLen = redisTemplate.opsForHash().get(Constant.REDISKEY_PEOPLECOUNT_PREFIX + DateUtil.getToday(), lastKey).toString();
        retMap.put("quelength", queLen);
        String attend = redisTemplate.opsForHash().get(Constant.REDISKEY_ATTENDANCEPROB_PREFIX + DateUtil.getToday(), lastKey).toString();
//        计算上座率和等待时长(秒)
        Double attProb = Double.valueOf(attend) / Constant.B1_FULLSEAT_PEOPLE;
        retMap.put("attendprob", attProb.toString());
        float waitTime = Integer.parseInt(queLen) / Constant.getPeopleFlowRate();
        retMap.put("waittime", waitTime);

        //todo: 获取历史排队人数
        Map<String, String> hisque = new LinkedHashMap<>();
        retMap.put("hisquecount", hisque);


        return ResultUtil.successResult(retMap);
    }

}
