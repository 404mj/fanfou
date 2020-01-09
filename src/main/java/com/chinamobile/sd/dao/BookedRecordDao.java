package com.chinamobile.sd.dao;

import com.chinamobile.sd.model.BookedRecord;
import com.chinamobile.sd.model.BookedRecordCount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: fengchen.zsx
 * @Date: 2020/1/3 14:11
 */
public interface BookedRecordDao {
    Integer addRecords(@Param("records") List<BookedRecord> records);

    List<BookedRecordCount> getRecordCountBetweenTime(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
