package com.chinamobile.sd.service;

import com.chinamobile.sd.commonUtils.IntegerUtil;
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
        if (StringUtils.isEmpty(day) || IntegerUtil.wrongInteger(period, 2) || IntegerUtil.wrongInteger(foodBelng, 1)) {
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
        if (StringUtils.isEmpty(day) || IntegerUtil.wrongInteger(period, 2) || IntegerUtil.wrongInteger(foodBelng, 1)) {
            return ResultUtil.failResult(ServiceEnum.VALIDATE_ERROR, ServiceEnum.VALIDATE_ERROR.getValue());
        }
        List<FoodItem> items = foodItemDao.findItemsByDayPeriod(day, period, foodBelng);
        return ResultUtil.successResult(items);
    }

    /**
     * @param itemList
     * @return
     */
    public ResultModel<Integer> addItems(List<FoodItem> itemList) throws Exception {
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
     * 赞踩的同时联动star信息
     *
     * @param restaurantId
     * @param foodId
     * @return
     */
    public ResultModel<Integer> upItem(Integer restaurantId, Integer foodId) {
        FoodItem foodItem = foodItemDao.findItemById(foodId);
        int star = starCaculator(foodItem, false);
        if (foodItem.getStars() != star) {
            foodItemDao.updateStar(foodId, star);
        }
        Integer res = foodItemDao.addItemUp(restaurantId, foodId);
        if (res > 0) {
            return ResultUtil.successResult(res);
        } else {
            return ResultUtil.failResult(ServiceEnum.UPDATE_ERROR, ServiceEnum.UPDATE_ERROR.getValue()
                    , ServiceEnum.UPDATE_ERROR.getCode());
        }
    }

    /**
     * @param restaurantId
     * @param foodId
     * @return
     */
    public ResultModel<Integer> downItem(Integer restaurantId, Integer foodId) {
        FoodItem foodItem = foodItemDao.findItemById(foodId);
        int star = starCaculator(foodItem, true);
        if (foodItem.getStars() != star) {
            foodItemDao.updateStar(foodId, star);
        }
        Integer res = foodItemDao.addItemDown(restaurantId, foodId);
        if (res > 0) {
            return ResultUtil.successResult(res);
        } else {
            return ResultUtil.failResult(ServiceEnum.UPDATE_ERROR, ServiceEnum.UPDATE_ERROR.getValue()
                    , ServiceEnum.UPDATE_ERROR.getCode());
        }
    }

    /**
     * @param foodId
     */
    public void removeItemById(Integer foodId) {
        foodItemDao.removeItemById(foodId);
    }

    /**
     * @param foodDay
     * @param rest
     * @return
     */
    public Integer removeItemsByDay(String foodDay, Integer rest) {
        return foodItemDao.removeItemByDayBelng(foodDay, rest);
    }


    /**
     * stars 与 赞踩的联动关系
     *
     * @param foodItem
     * @param isDown
     * @return
     */
    public int starCaculator(FoodItem foodItem, boolean isDown) {
        float oldUp = foodItem.getUp();
        float oldDown = foodItem.getDown();
        int fullStar = 5;
        float rate = oldDown / (oldUp + oldDown + 1);
        if (isDown) {
            rate = (oldDown + 1) / (oldUp + oldDown + 1);
        }
        if (rate > 0.5 && rate <= 0.66) {
            //扣1星
            fullStar -= 1;
        } else if (rate > 0.66 && rate <= 0.8) {
            //扣2星
            fullStar -= 2;
        } else if (rate > 0.8) {
            //扣3星
            fullStar -= 3;
        }
        return fullStar;
    }
}
