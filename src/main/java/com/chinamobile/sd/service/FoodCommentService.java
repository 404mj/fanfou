package com.chinamobile.sd.service;

import com.chinamobile.sd.commonUtils.DateUtil;
import com.chinamobile.sd.commonUtils.ResultUtil;
import com.chinamobile.sd.commonUtils.ServiceEnum;
import com.chinamobile.sd.dao.FoodCommentDao;
import com.chinamobile.sd.model.FoodComment;
import com.chinamobile.sd.model.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/30 9:35
 * <p>
 * 每日菜单评论服务
 */
@Service
public class FoodCommentService {

    @Autowired
    private FoodCommentDao foodCommentDao;

    public ResultModel<List<FoodComment>> getTodayComments() {
        return this.getCommentsByTime(DateUtil.getToday());
    }

    public ResultModel getCommentsByTime(String dayTime) {
        if (StringUtils.isEmpty(dayTime)) {
            return ResultUtil.failResult(ServiceEnum.INPUT_NULL, ServiceEnum.INPUT_NULL.getValue());
        }

        List<FoodComment> resList = foodCommentDao.findDiscussContentsByTime(dayTime);
        return ResultUtil.successResult(resList);
    }

    public ResultModel addComment(String content, String time) {
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(time)) {
            return ResultUtil.failResult(ServiceEnum.INPUT_NULL, ServiceEnum.INPUT_NULL.getValue());
        }

        Integer res = foodCommentDao.addComment(time, content);
        if (res > 0) {
            return ResultUtil.successResult(res);
        }
        return ResultUtil.failResult(ServiceEnum.SAVE_ERROR, ServiceEnum.SAVE_ERROR.getValue());
    }

}