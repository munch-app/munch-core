package com.munch.core.struct.database.place;

import com.munch.core.struct.database.abs.AbsAuditData;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalTime;

/**
 * Created By: Fuxing Loh
 * Date: 23/8/2016
 * Time: 4:59 PM
 * Project: struct
 */
@Entity
public class BusinessHour extends AbsAuditData {

    private String id;
    private int day;
    private LocalTime open;
    private LocalTime close;

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    @Id
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    @Column
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Column(nullable = false)
    public LocalTime getOpen() {
        return open;
    }

    public void setOpen(LocalTime open) {
        this.open = open;
    }

    @Column(nullable = false)
    public LocalTime getClose() {
        return close;
    }

    public void setClose(LocalTime close) {
        this.close = close;
    }
}
