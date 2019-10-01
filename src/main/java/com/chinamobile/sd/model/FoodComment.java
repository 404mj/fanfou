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
    private String discusser;


    public FoodComment(Integer commentId, String content, String commentTime, String discusser) {
        this.commentId = commentId;
        this.content = content;
        this.commentTime = commentTime;
        this.discusser = discusser;
    }

    @Override
    public String toString() {
        return "FoodComment{" +
                "commentId=" + commentId +
                ", content='" + content + '\'' +
                ", commentTime='" + commentTime + '\'' +
                ", discusser='" + discusser + '\'' +
                '}';
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
