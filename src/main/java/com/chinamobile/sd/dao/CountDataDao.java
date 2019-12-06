package com.chinamobile.sd.dao;

import com.chinamobile.sd.model.CountData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/12/5 16:31
 */
@Mapper
public interface CountDataDao {
    Integer addDatas(List<CountData> datas);
}
