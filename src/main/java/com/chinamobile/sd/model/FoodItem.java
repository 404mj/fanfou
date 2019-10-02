package com.chinamobile.sd.model;

import java.io.Serializable;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/29 17:38
 * <p>
 * 菜单详情
 */
public class FoodItem implements Serializable {

    private Integer foodId;
    private String foodDesc;
    private Integer kind;
    private Boolean recommend;
    private Integer period;
    private String foodTime;
    private Integer foodWeek;
    private Integer up;
    private Integer down;
    private String foodBelng;


    public FoodItem(Integer foodId, String foodDesc, Integer kind, Boolean recommend,
                    Integer period, String foodTime, Integer foodWeek, Integer up, Integer down, String foodBelng) {
        this.foodId = foodId;
        this.foodDesc = foodDesc;
        this.kind = kind;
        this.recommend = recommend;
        this.period = period;
        this.foodTime = foodTime;
        this.foodWeek = foodWeek;
        this.up = up;
        this.down = down;
        this.foodBelng = foodBelng;
    }

    @Override
    public String toString() {
        return "FoodItem{" +
                "foodId=" + foodId +
                ", foodDesc='" + foodDesc + '\'' +
                ", kind=" + kind +
                ", recommend=" + recommend +
                ", period=" + period +
                ", foodTime='" + foodTime + '\'' +
                ", foodWeek=" + foodWeek +
                ", up=" + up +
                ", down=" + down +
                ", foodBelng='" + foodBelng + '\'' +
                '}';
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getFoodTime() {
        return foodTime;
    }

    public void setFoodTime(String foodTime) {
        this.foodTime = foodTime;
    }

    public Integer getFoodWeek() {
        return foodWeek;
    }

    public void setFoodWeek(Integer foodWeek) {
        this.foodWeek = foodWeek;
    }

    public Integer getUp() {
        return up;
    }

    public void setUp(Integer up) {
        this.up = up;
    }

    public Integer getDown() {
        return down;
    }

    public void setDown(Integer down) {
        this.down = down;
    }

    public String getFoodBelng() {
        return foodBelng;
    }

    public void setFoodBelng(String foodBelng) {
        this.foodBelng = foodBelng;
    }
}
