package com.munch.core.struct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalTime;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 7:10 PM
 * Project: munch-core
 */
@Entity
public final class OpeningHours {

    public static final int MON = 1;
    public static final int TUE = 2;
    public static final int WED = 3;
    public static final int THU = 4;
    public static final int FRI = 5;
    public static final int SAT = 6;
    public static final int SUN = 7;

    public static final int PH = 100;

    private String id;

    private Integer day;
    private LocalTime open;
    private LocalTime close;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "CHAR(36)", nullable = false, updatable = false)
    @JsonIgnore
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    /**
     * @return day in integer
     * @see OpeningHours#MON
     * @see OpeningHours#SUN
     * @see OpeningHours#PH
     */
    @Column(nullable = false)
    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    /**
     * @return opening hours, start
     */
    @Column(nullable = false)
    public LocalTime getOpen() {
        return open;
    }

    public void setOpen(LocalTime open) {
        this.open = open;
    }

    /**
     * @return opening hours, end
     */
    @Column(nullable = false)
    public LocalTime getClose() {
        return close;
    }

    public void setClose(LocalTime close) {
        this.close = close;
    }
}
