package com.chinamobile.sd.dao;

import com.chinamobile.sd.model.BookedUser;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: fengchen.zsx
 * @Date: 2020/1/3 14:10
 */
public interface BookedUserDao {
    Integer addBookedUser(BookedUser buser);

    BookedUser findBookedUserByMemid(@Param("memId") String memId);
}
