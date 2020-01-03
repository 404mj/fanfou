package com.chinamobile.sd.service;

import com.chinamobile.sd.dao.BookedRecordDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: fengchen.zsx
 * @Date: 2020/1/3 14:42
 */
@Service
public class BookedRecordService {

    private Logger logger = LogManager.getLogger(BookedRecordService.class);

    @Autowired
    private BookedRecordDao recordDao;

}
