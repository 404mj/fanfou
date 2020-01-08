package com.chinamobile.sd.service;

import com.chinamobile.sd.dao.BookedUserDao;
import com.chinamobile.sd.model.BookedUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Author: fengchen.zsx
 * @Date: 2020/1/3 14:42
 */
@Service
public class BookedUserService {

    private Logger logger = LogManager.getLogger(BookedUserService.class);

    @Autowired
    private BookedUserDao buserDao;

    /**
     * @param buser
     * @return
     */
    public Integer newBuser(BookedUser buser) {
        buserDao.addBookedUser(buser);
        return buser.getBookUid();
    }

    public BookedUser getBuserByMemid(String memId) {
        return buserDao.findBookedUserByMemid(memId);
    }

}
