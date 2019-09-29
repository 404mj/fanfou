package com.chinamobile.sd.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/29 17:48
 */
@Mapper
public interface FoodDiscussDao {

    Integer addFoodDiscuss();
    Integer removeFoodDiscuss();

    List<String> findDiscussContent();

}
