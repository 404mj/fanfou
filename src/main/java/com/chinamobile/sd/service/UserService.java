package com.chinamobile.sd.service;

import com.chinamobile.sd.dao.UserDao;
import com.chinamobile.sd.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/22 9:34
 */

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public List<User> findAllUsers() {
        return userDao.findAllUsers();
    }


}
