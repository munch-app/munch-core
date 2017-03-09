package com.munch.struct;

import java.time.LocalTime;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 7:10 PM
 * Project: munch-core
 */
public final class Hours {

    public static final int MON = 1;
    public static final int TUE = 2;
    public static final int WED = 3;
    public static final int THU = 4;
    public static final int FRI = 5;
    public static final int SAT = 6;
    public static final int SUN = 7;

    public static final int PH = 100;

    private Integer day;
    private LocalTime open;
    private LocalTime close;

    /**
     * @return day in integer
     * @see Hours#MON
     * @see Hours#TUE
     * @see Hours#WED
     * @see Hours#THU
     * @see Hours#FRI
     * @see Hours#SAT
     * @see Hours#SUN
     * @see Hours#PH
     */
    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    /**
     * @return opening hours, start
     */
    public LocalTime getOpen() {
        return open;
    }

    public void setOpen(LocalTime open) {
        this.open = open;
    }

    /**
     * @return opening hours, end
     */
    public LocalTime getClose() {
        return close;
    }

    public void setClose(LocalTime close) {
        this.close = close;
    }
}
