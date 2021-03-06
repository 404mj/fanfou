package com.chinamobile.sd.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinamobile.sd.commonUtils.DateUtil;
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
import org.springframework.dao.DuplicateKeyException;
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


    @PostMapping("/add")
    public ResultModel bookFood(BookedUser bUser, @RequestBody String reqjson) {
        if (StringUtils.isEmpty(reqjson) || bUser == null) {
            return ResultUtil.failResult(ServiceEnum.INPUT_NULL, "参数有误");
        }
        Integer buid = null;
        try {
            BookedUser userInDb = buserService.getBuserByMemid(bUser.getMemId());
            if (userInDb == null) {
                buid = buserService.newBuser(bUser);
            } else {
                buid = userInDb.getBookUid();
            }
        } catch (Exception e) {
            return ResultUtil.failResult(ServiceEnum.NO_USER_INFO, ServiceEnum.NO_USER_INFO.getValue());
        }

        //处理预定请求
        List<BookedRecord> recordList = new ArrayList<>(32);
        JSONObject jsonObj = JSONObject.parseObject(reqjson);
        Integer rest = jsonObj.getInteger("restaurant");
        JSONArray bookItems = jsonObj.getJSONArray("book_items");
        for (int i = 0; i < bookItems.size(); ++i) {
            JSONObject item = bookItems.getJSONObject(i);
            String bdate = item.getString("date");
            JSONArray periods = item.getJSONArray("periods");

            if (!DateUtil.isFutureOfToday(bdate)) {
                return ResultUtil.failResult(ServiceEnum.VALIDATE_ERROR, ServiceEnum.VALIDATE_ERROR.getValue());
            }

            //处理预定条目
            if (periods.size() > 0) {
                for (Object period : periods.toArray()) {
                    BookedRecord br = new BookedRecord(buid, bdate, "", Integer.valueOf((String) period), rest);
                    recordList.add(br);
                }
            }
        }
        try {
            recordService.newRecords(recordList);
        } catch (DuplicateKeyException de) {
            return ResultUtil.failResult(ServiceEnum.SAVE_DUPLICATE, ServiceEnum.SAVE_DUPLICATE.getValue());
        } catch (Exception e) {
            return ResultUtil.failResult(ServiceEnum.SAVE_ERROR, ServiceEnum.SAVE_ERROR.getValue());
        }
        return ResultUtil.successResult(ServiceEnum.SUCCESS.getValue());
    }


    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportBookReq(@RequestParam("date_start") String dateStart,
                                                             @RequestParam("date_end") String dateEnd) {
        logger.info(dateStart);
        ByteArrayInputStream in = recordService.getCountAnd2Excel(dateStart, dateEnd);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=\"" + dateStart + "_" + dateEnd + ".xlsx" + "\"");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=utf-8"))
                .headers(headers)
                .body(new InputStreamResource(in));
    }


}
