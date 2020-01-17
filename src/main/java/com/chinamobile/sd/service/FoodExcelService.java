package com.chinamobile.sd.service;

import com.chinamobile.sd.commonUtils.Constant;
import com.chinamobile.sd.commonUtils.DateUtil;
import com.chinamobile.sd.commonUtils.ResultUtil;
import com.chinamobile.sd.commonUtils.ServiceEnum;
import com.chinamobile.sd.dao.FoodCommentDao;
import com.chinamobile.sd.dao.FoodItemDao;
import com.chinamobile.sd.model.FoodComment;
import com.chinamobile.sd.model.FoodItem;
import com.chinamobile.sd.model.ResultModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/10/8 21:35
 * <p>
 * 处理上传的菜单excel
 */
@Service
public class FoodExcelService {

    private static final Logger logger = LogManager.getLogger(FoodExcelService.class);

    @Autowired
    private FoodItemService foodItemService;
    @Autowired
    private FoodItemDao foodItemDao;
    @Autowired
    private FoodCommentDao foodCommentDao;

    public ResultModel<Integer> processRecipeExcel(MultipartFile recipeFile) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(recipeFile.getInputStream());
            if (workbook.getNumberOfSheets() >= 3) {
                XSSFSheet breakfast = workbook.getSheetAt(0);
                XSSFSheet lunch = workbook.getSheetAt(1);
                XSSFSheet dinner = workbook.getSheetAt(2);

                List<FoodItem> breakfastItems = processDishes(breakfast, 0);
                List<FoodItem> lunchItems = processDishes(lunch, 1);
                List<FoodItem> dinnerItems = processDishes(dinner, 2);

                List<FoodItem> weekFoods = Stream.of(breakfastItems, dinnerItems)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

//                logger.info("------weekfoods: " + breakfastItems.toString());
                return foodItemService.addItems(weekFoods);
            } else {
                logger.error("------sheet number err: " + workbook.getNumberOfSheets());
                return ResultUtil.failResult(ServiceEnum.PARAM_FORMAT_ERROR, ServiceEnum.PARAM_FORMAT_ERROR.getValue());
            }
        } catch (MultipartStream.IllegalBoundaryException e2) {
            logger.error(e2.getMessage(), e2);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ResultUtil.failResult(ServiceEnum.SAVE_ERROR, ServiceEnum.SAVE_ERROR.getValue());
    }


    /**
     * 修改菜的名字（不修改种类！！没有餐厅参数）
     *
     * @param itemDay
     * @param period
     * @param oldDesc
     * @param newDesc
     * @return
     */
    public int correctFoodItem(String itemDay, Integer period, String oldDesc, String newDesc) {
        return foodItemDao.modifyItemDesc(itemDay, period, oldDesc, newDesc);
    }

    /**
     * @param weekStart
     * @param weekEnd
     */
    public ByteArrayInputStream comments2Excel(List<FoodComment> comments) {
        String[] columns = {"菜品时间", "评论内容"};
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XSSFSheet sheet = workbook.createSheet("Comments");

            //表头设置&内容
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());
            XSSFCellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            XSSFRow headerRow = sheet.createRow(0);
            for (int col = 0; col < columns.length; col++) {
                XSSFCell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIdx = 1;
            for (FoodComment comment : comments) {
                XSSFRow row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(comment.getFoodTime());
                row.createCell(1).setCellValue(comment.getContent());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * @param periodRecipe
     * @param period
     * @return
     */
    private List<FoodItem> processDishes(XSSFSheet periodRecipe, Integer period) {
        List<FoodItem> foodItems = new ArrayList<>(32);

        int rows = periodRecipe.getLastRowNum();
        String foodBelng = periodRecipe.getRow(0).getCell(0).getStringCellValue();
        Integer restaurant = 0;
        if (foodBelng.equals("B1小餐厅")) {
            restaurant = 1;
        }

        //从第三行开始遍历每天的菜品
        for (int i = 2; i <= rows; ++i) {
            XSSFRow dayFood = periodRecipe.getRow(i);
            //处理推荐
            String[] recommend = dayFood.getCell(dayFood.getLastCellNum() - 1).getStringCellValue().trim()
                    .split(Constant.RECIPE_SEPRATE);
            Set<String> recs = new HashSet<>(Arrays.asList(recommend));
            //日期
            String thisDay = DateUtil.date2String(dayFood.getCell(0).getDateCellValue(), "");
            //星期
            String thisWeek = dayFood.getCell(1).getStringCellValue();
            //水果pass
            //凉菜
            String[] colds = dayFood.getCell(3).getStringCellValue().trim().split(Constant.RECIPE_SEPRATE);
            this.addNewItems(foodItems, colds, 1, period, thisDay, thisWeek, restaurant, recs);
            //热菜
            String[] hots = dayFood.getCell(4).getStringCellValue().trim().split(Constant.RECIPE_SEPRATE);
            this.addNewItems(foodItems, hots, 2, period, thisDay, thisWeek, restaurant, recs);
            //面食
            String[] wheats = dayFood.getCell(5).getStringCellValue().trim().split(Constant.RECIPE_SEPRATE);
            this.addNewItems(foodItems, wheats, 3, period, thisDay, thisWeek, restaurant, recs);
            //汤粥
            String[] soups = dayFood.getCell(6).getStringCellValue().trim().split(Constant.RECIPE_SEPRATE);
            this.addNewItems(foodItems, soups, 4, period, thisDay, thisWeek, restaurant, recs);
            //现场制作
            String[] handmakes = dayFood.getCell(7).getStringCellValue().trim().split(Constant.RECIPE_SEPRATE);
            this.addNewItems(foodItems, handmakes, 5, period, thisDay, thisWeek, restaurant, recs);
        }

        return foodItems;
    }

    /**
     * @param foodItems
     * @param dishes
     * @param kind
     * @param period
     * @param day
     * @param week
     * @param restaurant
     * @param recs
     */
    private void addNewItems(List<FoodItem> foodItems, String[] dishes, Integer kind, Integer period,
                             String day, String week, Integer restaurant, Set<String> recs) {
        for (String dish : dishes) {
            if (!StringUtils.isEmpty(dish) && !StringUtils.isEmpty(day)) {
                FoodItem item = new FoodItem(null, dish, kind, false, period, day, week,
                        0, 0, 5, restaurant);
                if (recs.contains(dish)) {
                    item.setRecommend(true);
                }
                foodItems.add(item);
            }
        }
    }

}
