package com.chinamobile.sd.controller;

import com.chinamobile.sd.commonUtils.ResultUtil;
import com.chinamobile.sd.commonUtils.ServiceEnum;
import com.chinamobile.sd.model.ResultModel;
import com.chinamobile.sd.service.StatisticService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/10/10 9:00
 * <p>
 * 处理数据请求
 */
@RestController
@RequestMapping("/fanfou/ai")
public class StatisticController {

    private Logger logger = LogManager.getLogger(StatisticController.class);

    @Autowired
    private StatisticService statisticService;

    /**
     * @param rid
     * @return
     */
    @GetMapping("/nowstatistic/restaurant/{rid}")
    public ResultModel getNowStatistic(@PathVariable("rid") Integer rid) {
        if (rid == null || rid < 0) {
            return ResultUtil.failResult(ServiceEnum.VALIDATE_ERROR, "error param");
        }

        ResultModel res = statisticService.getStatistic(rid);
        logger.info(res);
        return res;
    }

}
