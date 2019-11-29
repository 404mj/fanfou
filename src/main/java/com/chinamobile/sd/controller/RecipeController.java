package com.chinamobile.sd.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinamobile.sd.commonUtils.DateUtil;
import com.chinamobile.sd.commonUtils.ResultUtil;
import com.chinamobile.sd.commonUtils.ServiceEnum;
import com.chinamobile.sd.commonUtils.StringUtil;
import com.chinamobile.sd.model.FoodComment;
import com.chinamobile.sd.model.ResultModel;
import com.chinamobile.sd.service.FoodCommentService;
import com.chinamobile.sd.service.FoodExcelService;
import com.chinamobile.sd.service.FoodItemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;


/**
 * @Author: fengchen.zsx
 * @Date: 2019/10/10 9:00
 * <p>
 * 处理食谱相关请求
 */
@RestController
@RequestMapping("/fanfou/restaurant")
public class RecipeController {

    private static final Logger logger = LogManager.getLogger(RecipeController.class);

    @Autowired
    private FoodExcelService foodExcelService;
    @Autowired
    private FoodItemService foodItemService;
    @Autowired
    private FoodCommentService foodCommentService;

    /**
     * 上传excel菜谱
     */
    @PostMapping("/upload")
    public ResultModel uploadRecipe(@RequestParam("recipeFile") MultipartFile recipeFile) {
        if (recipeFile.getSize() == 0) {
            return ResultUtil.customResult(ServiceEnum.VALIDATE_ERROR, 0);
        }
        return foodExcelService.processRecipeExcel(recipeFile);
    }

    /**
     * 修改某个菜品
     *
     * @param reqjson
     * @return
     */
    @PostMapping("/correct")
    public ResultModel correctMenu(@RequestBody String reqjson) {
        if (StringUtils.isEmpty(reqjson)) {
            return ResultUtil.failResult(ServiceEnum.INPUT_NULL, "参数有误");
        }

        JSONObject jsonObject = JSONObject.parseObject(reqjson);
        String itemDay = jsonObject.getString("itemday");
        Integer period = jsonObject.getInteger("period");
        String oldDesc = jsonObject.getString("olddesc");
        String newDesc = jsonObject.getString("newdesc");
        if (StringUtils.isEmpty(itemDay) || StringUtils.isEmpty(period) || StringUtils.isEmpty(oldDesc)
                || StringUtils.isEmpty(newDesc)) {
            return ResultUtil.failResult(ServiceEnum.INPUT_NULL, "参数有误");
        }

        int res = foodExcelService.correctFoodItem(itemDay, period, oldDesc, newDesc);
        if (res > 0) {
            return ResultUtil.successResult(res);
        }
        return ResultUtil.failResult(ServiceEnum.UPDATE_ERROR, "", "提供信息有误，无法进行修改");
    }

    /**
     * @param rid
     * @param req
     * @return
     */
    @PostMapping("/{rid}/getrecommend")
    public ResultModel getRecommendItems(@PathVariable("rid") Integer rid, @RequestBody String req) {
        if (rid == null || rid < 0 || StringUtils.isEmpty(req)) {
            return ResultUtil.failResult(ServiceEnum.INPUT_NULL, "error param request");
        }

        JSONObject reqjson = JSON.parseObject(req);
        String day = reqjson.getString("daytime");
        Integer period = reqjson.getInteger("period");

        return foodItemService.getRecommendTodayPeriod(day, period, rid);
    }

    /**
     * @param rid
     * @param req
     * @return
     */
    @PostMapping("/{rid}/getfoods")
    public ResultModel getAllFoods(@PathVariable("rid") Integer rid, @RequestBody String req) {
        if (rid == null || rid < 0 || StringUtils.isEmpty(req)) {
            return ResultUtil.failResult(ServiceEnum.INPUT_NULL, "error param request");
        }

        JSONObject reqjson = JSON.parseObject(req);
        String day = reqjson.getString("daytime");
        Integer period = reqjson.getInteger("period");

        return foodItemService.getItemsByDayAndPeriod(day, period, rid);
    }


    /**
     * @param rid
     * @param req
     * @return
     */
    @PostMapping("/{rid}/getcomments")
    public ResultModel getDayComments(@PathVariable("rid") Integer rid, @RequestBody String req) {
        if (rid == null || rid < 0 || StringUtils.isEmpty(req)) {
            return ResultUtil.failResult(ServiceEnum.INPUT_NULL, "error param request");
        }

        JSONObject reqjson = JSON.parseObject(req);
//        String day = reqjson.getString("food_time");

        String[] days = DateUtil.getCurrentWeekFirstLastDay();
        return foodCommentService.getCommentsBetweenTime(days[0], days[1], rid);
//        return foodCommentService.getCommentsByDay(day, rid);
    }


    /**
     * @param rid
     * @param req
     * @return
     */
    @PostMapping("/{rid}/comment")
    public ResultModel commentFood(@PathVariable("rid") Integer rid, @RequestBody String req) {
        if (rid == null || rid < 0 || StringUtils.isEmpty(req)) {
            return ResultUtil.failResult(ServiceEnum.INPUT_NULL, "error param request");
        }

        JSONObject reqjson = JSON.parseObject(req);
        String content = reqjson.getString("comment_content");
        String foodTime = reqjson.getString("food_time");
        String commentTime = DateUtil.getTodayWithSlash();
        return foodCommentService.addComment(content, commentTime, rid, foodTime);
    }

    /**
     * @param rid
     * @param foodId
     * @return
     */
    @PostMapping("/{rid}/up/{foodId}")
    public ResultModel addFoodUp(@PathVariable("rid") Integer rid, @PathVariable("foodId") Integer foodId) {
        if (rid == null || foodId == null || rid < 0 || foodId <= 0) {
            return ResultUtil.failResult(ServiceEnum.INPUT_NULL, "error param request");
        }
        return foodItemService.upItem(rid, foodId);
    }


    /**
     * @param rid
     * @param foodId
     * @return
     */
    @PostMapping("/{rid}/down/{foodId}")
    public ResultModel addFoodDown(@PathVariable("rid") Integer rid, @PathVariable("foodId") Integer foodId) {
        if (rid == null || foodId == null || rid < 0 || foodId <= 0) {
            return ResultUtil.failResult(ServiceEnum.INPUT_NULL, "error param request");
        }
        return foodItemService.downItem(rid, foodId);
    }


    /**
     * @param weekStart
     * @param weekEnd
     * @return
     */
    @GetMapping("/comments/export/")
    public ResponseEntity<InputStreamResource> getCommentsExcel(@PathVariable("weekStart") String weekStart,
                                                                @PathVariable("weekEnd") String weekEnd) {

        if (StringUtils.isEmpty(weekEnd) || StringUtils.isEmpty(weekStart)) {
            return null;
        }
        ResultModel<List<FoodComment>> comments = foodCommentService.getCommentsBetweenTime(weekStart, weekEnd, 0);
        ByteArrayInputStream in = foodExcelService.comments2Excel((List<FoodComment>) comments.getData());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }

}
