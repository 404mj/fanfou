package com.chinamobile.sd.dao;

import com.chinamobile.sd.model.FoodComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/29 17:48
 */
@Mapper
public interface FoodCommentDao {

    Integer addComment(@Param("day") String day, @Param("content") String content,
                       @Param("restaurant") Integer restaurant, @Param("foodTime") String foodTime);

    List<FoodComment> findDiscussContentsByTime(@Param("foodTime") String foodTime, @Param("restaurant") Integer restaurant);

    List<FoodComment> findDiscussContentsByWeek(@Param("weekStart") String weekStart, @Param("weekEnd") String weekEnd,
                                                @Param("restaurant") Integer restaurant);

}
