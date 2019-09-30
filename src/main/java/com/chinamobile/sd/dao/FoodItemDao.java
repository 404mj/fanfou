package com.chinamobile.sd.dao;

import com.chinamobile.sd.model.FoodItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/29 17:48
 */
@Mapper
public interface FoodItemDao {

    List<FoodItem> findRecommendByDayPeriod(@Param("day") String day, @Param("period") Integer period);

    List<FoodItem> findItemByWeekPeriod(@Param("week") Integer week, @Param("period") Integer period);

    Integer createItems();

    Integer addItem(@Param("foodItem") FoodItem foodItem);

    Integer removeItemById(@Param("itemId") Integer itemId);

    Integer removeItemByDay(@Param("day") String day);

    Integer addItemUp(@Param("foodId") Integer foodId);

    Integer addItemDown(@Param("foodId") Integer foodId);

//    Integer modifyItemByTime(FoodItem foodItem);

}
