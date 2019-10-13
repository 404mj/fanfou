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

    List<FoodItem> findRecommendByDayPeriod(@Param("day") String day, @Param("period") Integer period,
                                            @Param("foodBelng") Integer foodBelng);


    List<FoodItem> findItemsByDayPeriod(@Param("day") String day, @Param("period") Integer period,
                                        @Param("foodBelng") Integer foodBelng);


    Integer createItems(List<FoodItem> itemList);

    Integer addItem(FoodItem foodItem);

    Integer removeItemById(@Param("itemId") Integer itemId);

    Integer removeItemByDayBelng(@Param("day") String day, @Param("foodBelng") Integer foodBelng);

    Integer addItemUp(@Param("foodId") Integer foodId);

    Integer addItemDown(@Param("foodId") Integer foodId);

    Integer modifyItemDesc(@Param("itemDay") String itemDay, @Param("period") Integer period,
                           @Param("oldDesc") String oldDesc, @Param("newDesc") String newDesc);

}
