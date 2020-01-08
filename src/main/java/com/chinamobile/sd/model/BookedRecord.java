package com.chinamobile.sd.model;

/**
 * @Author: fengchen.zsx
 * @Date: 2020/1/3 14:00
 */
public class BookedRecord {
    private Integer bookId;
    private Integer bookUid;
    private String bookTime;
    private String bookWeek;
    private Integer bookPeriod;
    private Integer bookRest;
    private Integer count;

    public BookedRecord(Integer bookUid, String bookTime, String bookWeek, Integer bookPeriod, Integer bookRest) {
        this.bookUid = bookUid;
        this.bookTime = bookTime;
        this.bookWeek = bookWeek;
        this.bookPeriod = bookPeriod;
        this.bookRest = bookRest;
    }

    @Override
    public String toString() {
        return "BookedRecordDao{" +
                "bookId=" + bookId +
                ", bookUid=" + bookUid +
                ", bookTime='" + bookTime + '\'' +
                ", bookWeek='" + bookWeek + '\'' +
                ", bookPeriod=" + bookPeriod +
                ", bookRest=" + bookRest +
                '}';
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getBookUid() {
        return bookUid;
    }

    public void setBookUid(Integer bookUid) {
        this.bookUid = bookUid;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }

    public String getBookWeek() {
        return bookWeek;
    }

    public void setBookWeek(String bookWeek) {
        this.bookWeek = bookWeek;
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
