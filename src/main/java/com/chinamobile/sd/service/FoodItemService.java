package com.chinamobile.sd.service;

import com.chinamobile.sd.commonUtils.ResultUtil;
import com.chinamobile.sd.commonUtils.ServiceEnum;
import com.chinamobile.sd.dao.FoodItemDao;
import com.chinamobile.sd.model.FoodItem;
import com.chinamobile.sd.model.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    /**
     * @param day
     * @param period
     * @return
     */
    public ResultModel<List<FoodItem>> getRecommendTodayPeriod(String day, Integer period, Integer foodBelng) {
        if (StringUtils.isEmpty(day) || period < 0 || period >= 3 || foodBelng < 0) {
            return ResultUtil.failResult(ServiceEnum.VALIDATE_ERROR, ServiceEnum.VALIDATE_ERROR.getValue());
        }
        List<FoodItem> items = foodItemDao.findRecommendByDayPeriod(day, period, foodBelng);
        return ResultUtil.successResult(items);
    }

    /**
     * @param day
     * @param period
     * @return
     */
    public ResultModel<List<FoodItem>> getItemsByDayAndPeriod(String day, Integer period, Integer foodBelng) {
        if (StringUtils.isEmpty(day) || period < 0 || period >= 3 || foodBelng < 0) {
            return ResultUtil.failResult(ServiceEnum.VALIDATE_ERROR, ServiceEnum.VALIDATE_ERROR.getValue());
        }
        List<FoodItem> items = foodItemDao.findItemsByDayPeriod(day, period, foodBelng);
        return ResultUtil.successResult(items);
    }

    /**
     * @param itemList
     * @return
     */
    public ResultModel<Integer> addItems(List<FoodItem> itemList) {
        if (null == itemList || itemList.isEmpty()) {
            return ResultUtil.failResult(ServiceEnum.INPUT_NULL, ServiceEnum.INPUT_NULL.getValue());
        }

        Integer insertRes = foodItemDao.createItems(itemList);
        if (insertRes == itemList.size()) {
            return ResultUtil.successResult(insertRes);
        }
        return ResultUtil.failResult(ServiceEnum.SAVE_ERROR, ServiceEnum.SAVE_ERROR.getValue());
    }

    /**
     * @param foodItem
     * @return
     */
    public ResultModel<Integer> addItem(FoodItem foodItem) {
        if (null == foodItem) {
            return ResultUtil.failResult(ServiceEnum.INPUT_NULL, ServiceEnum.INPUT_NULL.getValue());
        }

        Integer insertRes = foodItemDao.addItem(foodItem);
        return ResultUtil.successResult(insertRes);
    }

    /**
     * @param restaurantId
     * @param foodId
     * @return
     */
    public ResultModel<Integer> upItem(Integer restaurantId, Integer foodId) {
        //todo stars 与 赞踩的联动关系
        Integer res = foodItemDao.addItemUp(restaurantId, foodId);
        return ResultUtil.successResult(res);
    }

    /**
     * @param restaurantId
     * @param foodId
     * @return
     */
    public ResultModel<Integer> downItem(Integer restaurantId, Integer foodId) {
        Integer res = foodItemDao.addItemDown(restaurantId, foodId);
        return ResultUtil.successResult(res);
    }

    public void removeItemById(Integer foodId) {
        foodItemDao.removeItemById(foodId);
    }

}
