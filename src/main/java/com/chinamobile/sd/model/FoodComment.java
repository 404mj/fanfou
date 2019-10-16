package com.chinamobile.sd.model;

import java.io.Serializable;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/29 17:38
 * <p>
 * 菜品评价
 */
public class FoodComment implements Serializable {

    private Integer commentId;
    private String content;
    private String commentTime;
    private String foodTime;
    private String discusser;
    private String restaurant;

    public FoodComment(Integer commentId, String content, String commentTime,
                       String foodTime, String discusser, String restaurant) {
        this.commentId = commentId;
        this.content = content;
        this.commentTime = commentTime;
        this.foodTime = foodTime;
        this.discusser = discusser;
        this.restaurant = restaurant;
    }

    public String getFoodTime() {
        return foodTime;
    }

    public void setFoodTime(String foodTime) {
        this.foodTime = foodTime;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getDiscusser() {
        return discusser;
    }

    public void setDiscusser(String discusser) {
        this.discusser = discusser;
    }
}
