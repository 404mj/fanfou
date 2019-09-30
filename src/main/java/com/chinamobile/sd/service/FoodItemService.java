package com.chinamobile.sd.service;

import com.chinamobile.sd.dao.FoodItemDao;
import com.chinamobile.sd.model.FoodItem;
import com.chinamobile.sd.model.ResultModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/30 9:35
 * <p>
 * 菜单详情服务
 */
@Service
public class FoodItemService {

    @Autowired
    private FoodItemDao foodItemDao;

    public ResultModel<List<FoodItem>> getRecommendTodayPeriod(String day, Integer period) {

        return null;
    }

}
