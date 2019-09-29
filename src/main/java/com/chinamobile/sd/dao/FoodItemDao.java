package com.chinamobile.sd.dao;

import com.chinamobile.sd.model.FoodItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/29 17:48
 */
@Mapper
public interface FoodItemDao {

    List<FoodItem> findRecommendByDayPeriod();

    List<FoodItem> findItemByWeekPeriod();

    Integer createItem();

    Integer addItemUp();

    Integer addItemDown();

    Integer modifyItemByTime();

    Integer deleteFoodItem();

}
