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

    Integer addComment(@Param("day") String day, @Param("content") String content, @Param("restaurant") Integer restaurant);

    List<FoodComment> findDiscussContentsByTime(@Param("day") String day, @Param("restaurant") Integer restaurant);

}
