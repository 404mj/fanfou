package com.chinamobile.sd.dao;

import com.chinamobile.sd.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/22 8:27
 */
@Mapper
public interface UserDao {
    List<User> findAllUsers();
}
