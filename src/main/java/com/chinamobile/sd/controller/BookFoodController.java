package com.chinamobile.sd.controller;

import com.chinamobile.sd.service.BookedRecordService;
import com.chinamobile.sd.service.BookedUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: fengchen.zsx
 * @Date: 2020/1/3 15:44
 */
@RestController
@RequestMapping("/fanfou/book/")
public class BookFoodController {
    private Logger logger = LogManager.getLogger(BookFoodController.class);

    @Autowired
    private BookedUserService buserService;
    @Autowired
    private BookedRecordService recordService;




}
