package com.munch.core.struct.block.source;

import com.munch.core.struct.block.BlockVersion;

import java.time.LocalTime;

/**
 * Created By: Fuxing Loh
 * Date: 26/8/2016
 * Time: 5:27 PM
 * Project: struct
 */
public class SourceHour extends BlockVersion {

    private int day;
    private String open;
    private String close;

    /**
     * All data should be named with the version that is introduced
     * For data not named, it is there since VERSION_FIRST
     */
    public SourceHour() {
        super(VERSION_FIRST);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public LocalTime getOpenLocal() {
        return LocalTime.parse(open);
    }

    public void setOpen(LocalTime open) {
        this.open = open.toString();
    }

    public LocalTime getCloseLocal() {
        return LocalTime.parse(close);
    }

    public void setClose(LocalTime close) {
        this.close = close.toString();
    }
}
