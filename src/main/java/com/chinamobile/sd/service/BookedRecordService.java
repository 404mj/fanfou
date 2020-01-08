package com.chinamobile.sd.service;

import com.chinamobile.sd.dao.BookedRecordDao;
import com.chinamobile.sd.model.BookedRecord;
import com.chinamobile.sd.model.FoodComment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @Author: fengchen.zsx
 * @Date: 2020/1/3 14:42
 */
@Service
public class BookedRecordService {

    private Logger logger = LogManager.getLogger(BookedRecordService.class);

    @Autowired
    private BookedRecordDao recordDao;

    /**
     * @param recordList
     * @return
     */
    public Integer newRecords(List<BookedRecord> recordList) {
        return recordDao.addRecords(recordList);
    }


    /**
     * @param dateS
     * @param dateE
     * @return
     */
    public List<BookedRecord> getRecordsCountsBetweenDates(String dateS, String dateE) {
        List<BookedRecord> recordCounts = recordDao.getRecordCountBetweenTime(dateS, dateE);
        return recordCounts;
    }

    /**
     * @param dateS
     * @param dateE
     * @return
     */
    public ByteArrayInputStream getCountAnd2Excel(String dateS, String dateE) {
//        List<BookedRecord> countList = getRecordsCountsBetweenDates(dateS, dateE);
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XSSFSheet sheet = workbook.createSheet("用餐情况反馈");

            //表头样式
            XSSFCellStyle headerCellStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle.setBorderBottom(BorderStyle.THIN);
            headerCellStyle.setBorderRight(BorderStyle.THIN);
            //表格样式
            XSSFCellStyle tableCellStyle = workbook.createCellStyle();
            tableCellStyle.setAlignment(HorizontalAlignment.CENTER);
            tableCellStyle.setBorderBottom(BorderStyle.THIN);
            tableCellStyle.setBorderRight(BorderStyle.THIN);


            //大标题
            XSSFRow headerRow = sheet.createRow(0);
            CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 9);
            RegionUtil.setBorderBottom(BorderStyle.THIN, cra, sheet);
            sheet.addMergedRegion(cra);
            XSSFCell bigTitle = headerRow.createCell(0, CellType.STRING);
            headerRow.createCell(9).setCellStyle(headerCellStyle);
            bigTitle.setCellValue("省公司餐厅非工作日就餐情况统计");
            bigTitle.setCellStyle(headerCellStyle);

            //副标题&表头
            XSSFRow row2 = sheet.createRow(1);
            XSSFRow row3 = sheet.createRow(2);
            CellRangeAddress cra2 = new CellRangeAddress(1, 2, 0, 0);
            sheet.addMergedRegion(cra2);
            RegionUtil.setBorderBottom(BorderStyle.THIN, cra2, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, cra2, sheet);
            XSSFCell addateTitle = row2.createCell(0, CellType.STRING);
            addateTitle.setCellValue("用餐日期/地点");
            addateTitle.setCellStyle(tableCellStyle);

            CellRangeAddress cellRest0 = new CellRangeAddress(1, 1, 1, 3);
            sheet.addMergedRegion(cellRest0);
            RegionUtil.setBorderBottom(BorderStyle.THIN, cellRest0, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, cellRest0, sheet);
            XSSFCell rest0Title = row2.createCell(1);
            rest0Title.setCellValue("机关餐厅");
            rest0Title.setCellStyle(tableCellStyle);

            CellRangeAddress cellRest2 = new CellRangeAddress(1, 1, 4, 6);
            sheet.addMergedRegion(cellRest2);
            RegionUtil.setBorderBottom(BorderStyle.THIN, cellRest2, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, cellRest2, sheet);
            XSSFCell rest2Title = row2.createCell(4);
            rest2Title.setCellValue("七里山餐厅");
            rest2Title.setCellStyle(tableCellStyle);

            CellRangeAddress cellRest3 = new CellRangeAddress(1, 1, 7, 9);
            sheet.addMergedRegion(cellRest3);
            RegionUtil.setBorderBottom(BorderStyle.THIN, cellRest3, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, cellRest3, sheet);
            XSSFCell rest3Title = row2.createCell(7);
            rest3Title.setCellValue("德亨餐厅");
            rest3Title.setCellStyle(tableCellStyle);
            for (int i = 1; i <= 9; ++i) {
                XSSFCell cell = row3.createCell(i);
                if (i % 3 == 1) {
                    cell.setCellValue("早餐（人）");
                    cell.setCellStyle(tableCellStyle);
                } else if (i % 3 == 2) {
                    cell.setCellValue("午餐（人）");
                    cell.setCellStyle(tableCellStyle);
                } else if (i % 3 == 0) {
                    cell.setCellValue("晚餐（人）");
                    cell.setCellStyle(tableCellStyle);
                }
            }

            //写入统计信息
            for (int i = 3; i <= 6; ++i) {
                XSSFRow rowi = sheet.createRow(i);
                for (int j = 0; j <= 9; ++j) {
                    XSSFCell cellij = rowi.createCell(j);
                    cellij.setCellStyle(tableCellStyle);
                }
            }


            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
