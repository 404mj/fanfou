package com.chinamobile.sd.controller;

import com.chinamobile.sd.commonUtils.DateUtil;
import com.chinamobile.sd.commonUtils.ResultUtil;
import com.chinamobile.sd.commonUtils.ServiceEnum;
import com.chinamobile.sd.model.FoodItem;
import com.chinamobile.sd.model.ResultModel;
import com.chinamobile.sd.service.FoodExcelService;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
        List<FoodItem> res = foodExcelService.processRecipeExcel(recipeFile);

        return ResultUtil.successResult(res);
    }

}
