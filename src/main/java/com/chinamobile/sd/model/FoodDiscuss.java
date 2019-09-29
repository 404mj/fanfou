package com.chinamobile.sd.model;

import java.io.Serializable;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/29 17:38
 * <p>
 * 菜品评价
 */
public class FoodDiscuss implements Serializable {

    private String discussContent;
    private String discussTime;
    private String discusser;

    public FoodDiscuss(String discussContent, String discussTime) {
        this.discussContent = discussContent;
        this.discussTime = discussTime;
    }

    @Override
    public String toString() {
        return "FoodDiscuss{" +
                "discussContent='" + discussContent + '\'' +
                ", discussTime='" + discussTime + '\'' +
                '}';
    }

    public String getDiscussContent() {
        return discussContent;
    }

    public void setDiscussContent(String discussContent) {
        this.discussContent = discussContent;
    }

    public String getDiscussTime() {
        return discussTime;
    }

    public void setDiscussTime(String discussTime) {
        this.discussTime = discussTime;
    }

    public String getDiscusser() {
        return discusser;
    }

    public void setDiscusser(String discusser) {
        this.discusser = discusser;
    }
}
