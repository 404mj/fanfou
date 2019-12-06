package com.chinamobile.sd.model;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/12/5 16:28
 */
public class CountData {
    private Integer dataId;
    private Integer restaurant;
    private Integer countType;
    private String countKey;
    private Integer countValue;

    public CountData(Integer dataId, Integer restaurant, Integer countType, String countKey, Integer countValue) {
        this.dataId = dataId;
        this.restaurant = restaurant;
        this.countType = countType;
        this.countKey = countKey;
        this.countValue = countValue;
    }

    @Override
    public String toString() {
        return "CountData{" +
                "dataId=" + dataId +
                ", restaurant=" + restaurant +
                ", countType=" + countType +
                ", countKey='" + countKey + '\'' +
                ", countValue=" + countValue +
                '}';
    }

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public Integer getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Integer restaurant) {
        this.restaurant = restaurant;
    }

    public Integer getCountType() {
        return countType;
    }

    public void setCountType(Integer countType) {
        this.countType = countType;
    }

    public String getCountKey() {
        return countKey;
    }

    public void setCountKey(String countKey) {
        this.countKey = countKey;
    }

    public Integer getCountValue() {
        return countValue;
    }

    public void setCountValue(Integer countValue) {
        this.countValue = countValue;
    }
}
