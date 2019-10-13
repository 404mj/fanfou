package com.chinamobile.sd.controller;

import com.alibaba.fastjson.JSONObject;
import com.chinamobile.sd.commonUtils.DateUtil;
import com.chinamobile.sd.commonUtils.ResultUtil;
import com.chinamobile.sd.commonUtils.ServiceEnum;
import com.chinamobile.sd.commonUtils.StringUtil;
import com.chinamobile.sd.model.FoodItem;
import com.chinamobile.sd.model.ResultModel;
import com.chinamobile.sd.service.FoodExcelService;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);

    @Autowired
    private FoodExcelService foodExcelService;

    /**
     * 上传excel菜谱
     */
    @PostMapping("/upload")
    public ResultModel uploadRecipe(@RequestParam("recipeFile") MultipartFile recipeFile) {
        if (recipeFile.getSize() == 0) {
            return ResultUtil.customResult(ServiceEnum.VALIDATE_ERROR, 0);
        }
        int added = foodExcelService.processRecipeExcel(recipeFile);

        return ResultUtil.successResult(added);
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
        return ResultUtil.failResult(ServiceEnum.SAVE_ERROR, "导入失败");
    }

}
