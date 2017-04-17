package munch.struct.place;

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

    /**
     * HH:mm
     * 00:00 - 23:59
     * Midnight - 1 Min before midnight Max
     * <p>
     * 12:00 - 22:00
     * Noon - 10pm
     * <p>
     * 08:00 - 19:00
     * 8am - 7pm
     * <p>
     * Not Allowed:
     * 20:00 - 04:00
     * 20:00 - 24:00
     * Not allowed to put 24:00 Highest is 23:59
     * Not allowed to cross to another day
     */
    private String open;
    private String close;

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
     * @return opening hours
     */
    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    /**
     * @return closing hours
     */
    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }
}
