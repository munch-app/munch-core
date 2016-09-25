package com.munch.core.struct.block.source.finals;

import java.time.LocalTime;

/**
 * Created By: Fuxing Loh
 * Date: 25/9/2016
 * Time: 4:51 PM
 * Project: struct
 */
public class FinalHourContext {

    private int day;
    private LocalTime open;
    private LocalTime close;

    private String left;
    private String right;

    private int source;
    private String sourceUrl;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public LocalTime getOpen() {
        return open;
    }

    public void setOpen(LocalTime open) {
        this.open = open;
    }

    public LocalTime getClose() {
        return close;
    }

    public void setClose(LocalTime close) {
        this.close = close;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
