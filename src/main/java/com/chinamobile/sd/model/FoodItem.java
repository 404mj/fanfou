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
    /**
     * 食物的种类:0-水果;1-凉菜;2-热菜;3-面点;4-汤粥;5-现场制作;
     */
    private Integer kind;
    private Boolean recommend;
    private Integer period;
    private String foodTime;
    private String foodWeek;
    private Integer up;
    private Integer down;
    private Integer stars;
    private Integer foodBelng;


    public FoodItem(Integer foodId, String foodDesc, Integer kind,
                    Boolean recommend, Integer period, String foodTime,
                    String foodWeek, Integer up, Integer down, Integer stars,
                    Integer foodBelng) {
        this.foodId = foodId;
        this.foodDesc = foodDesc;
        this.kind = kind;
        this.recommend = recommend;
        this.period = period;
        this.foodTime = foodTime;
        this.foodWeek = foodWeek;
        this.up = up;
        this.down = down;
        this.stars = stars;
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
                ", stars=" + stars +
                ", foodBelng='" + foodBelng + '\'' +
                '}';
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
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

    public String getFoodWeek() {
        return foodWeek;
    }

    public void setFoodWeek(String foodWeek) {
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

    public Integer getFoodBelng() {
        return foodBelng;
    }

    public void setFoodBelng(Integer foodBelng) {
        this.foodBelng = foodBelng;
    }
}
