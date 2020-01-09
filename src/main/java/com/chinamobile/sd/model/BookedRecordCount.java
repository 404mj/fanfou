package com.chinamobile.sd.model;

/**
 * @Author: fengchen.zsx
 * @Date: 2020/1/3 14:00
 */
public class BookedRecordCount {
    private String bookTime;
    private Integer bookRest;
    private Integer bookPeriod;
    private Integer count;

    public BookedRecordCount(String bookTime, Integer bookPeriod, Integer bookRest, Integer count) {
        this.bookTime = bookTime;
        this.bookPeriod = bookPeriod;
        this.bookRest = bookRest;
        this.count = count;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }

    public Integer getBookPeriod() {
        return bookPeriod;
    }

    public void setBookPeriod(Integer bookPeriod) {
        this.bookPeriod = bookPeriod;
    }

    public Integer getBookRest() {
        return bookRest;
    }

    public void setBookRest(Integer bookRest) {
        this.bookRest = bookRest;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
