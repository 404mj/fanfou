package com.chinamobile.sd.model;

import java.io.Serializable;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/29 17:38
 * <p>
 * 菜品评价
 */
public class FoodComment implements Serializable {

    private String content;
    private String commentTime;
    private String discusser;

    public FoodComment(String content, String commentTime) {
        this.content = content;
        this.commentTime = commentTime;
    }

    @Override
    public String toString() {
        return "FoodComment{" +
                "content='" + content + '\'' +
                ", commentTime='" + commentTime + '\'' +
                ", discusser='" + discusser + '\'' +
                '}';
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
