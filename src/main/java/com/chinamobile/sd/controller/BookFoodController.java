package com.chinamobile.sd.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinamobile.sd.commonUtils.ResultUtil;
import com.chinamobile.sd.commonUtils.ServiceEnum;
import com.chinamobile.sd.model.BookedRecord;
import com.chinamobile.sd.model.BookedUser;
import com.chinamobile.sd.model.ResultModel;
import com.chinamobile.sd.service.BookedRecordService;
import com.chinamobile.sd.service.BookedUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: fengchen.zsx
 * @Date: 2020/1/3 15:44
 */
@RestController
@RequestMapping("/fanfou/book")
public class BookFoodController {
    private Logger logger = LogManager.getLogger(BookFoodController.class);

    @Autowired
    private BookedUserService buserService;
    @Autowired
    private BookedRecordService recordService;


    @RequestMapping("/add")
    public ResultModel bookFood(BookedUser bUser, @RequestBody String reqjson) {
        if (StringUtils.isEmpty(reqjson) || bUser == null) {
            return ResultUtil.failResult(ServiceEnum.INPUT_NULL, "参数有误");
        }

        Integer buid = null;
        BookedUser userInDb = buserService.getBuserByMemid(bUser.getMemId());
        if (userInDb == null) {
            buid = buserService.newBuser(bUser);
        } else {
            buid = userInDb.getBookUid();
        }


        //处理预定请求
        List<BookedRecord> recordList = new ArrayList<>(32);
        JSONObject jsonObj = JSONObject.parseObject(reqjson);
        Integer rest = jsonObj.getInteger("restaurant");
        JSONArray bookItems = jsonObj.getJSONArray("book_items");
        for (int i = 0; i < bookItems.size(); ++i) {
            JSONObject item = bookItems.getJSONObject(i);
            String bdate = item.getString("date");
            String periods = item.getString("periods");

            //处理预定条目
            if (periods.length() > 1) {
                String[] ps = periods.split(",");
                for (String period : ps) {
                    BookedRecord br = new BookedRecord(buid, bdate, "", Integer.valueOf(period), rest);
                    recordList.add(br);
                }
            } else {
                BookedRecord br = new BookedRecord(buid, bdate, "", Integer.valueOf(periods), rest);
                recordList.add(br);
            }
        }
        Integer res = recordService.newRecords(recordList);
        if (res > 0) {
            return ResultUtil.successResult(res);
        }
        return ResultUtil.failResult(ServiceEnum.SAVE_ERROR, ServiceEnum.SAVE_ERROR.getValue());
    }


    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportBookReq() {
//        if (StringUtils.isEmpty(reqjson)) {
//            return null;
//        }
//        JSONObject jsonObj = JSONObject.parseObject(reqjson);
        String dateStart = "2020-01-11";// jsonObj.getString("date_start");
        String dateEnd = "2020-01-12";//jsonObj.getString("date_end");

        ByteArrayInputStream in = recordService.getCountAnd2Excel(dateStart, dateEnd);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=\"" + dateStart + "_" + dateEnd + ".xlsx" + "\"");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=utf-8"))
                .headers(headers)
                .body(new InputStreamResource(in));
    }


}
