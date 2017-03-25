package munch.struct;

import java.time.LocalTime;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 7:10 PM
 * Project: munch-core
 */
public final class Hour {

    public static final int MON = 1;
    public static final int TUE = 2;
    public static final int WED = 3;
    public static final int THU = 4;
    public static final int FRI = 5;
    public static final int SAT = 6;
    public static final int SUN = 7;

    public static final int PH = 100;

    private int day;
    private LocalTime open;
    private LocalTime close;

    /**
     * @return day in int
     * @see Hour#MON
     * @see Hour#TUE
     * @see Hour#WED
     * @see Hour#THU
     * @see Hour#FRI
     * @see Hour#SAT
     * @see Hour#SUN
     * @see Hour#PH
     */
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
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
