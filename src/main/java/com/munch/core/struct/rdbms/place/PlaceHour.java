package com.munch.core.struct.rdbms.place;

import com.munch.core.struct.rdbms.abs.AbsAuditData;
import com.munch.core.struct.rdbms.abs.HashSetData;
import com.munch.core.struct.util.map.ManyEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalTime;

/**
 * Created By: Fuxing Loh
 * Date: 23/8/2016
 * Time: 4:59 PM
 * Project: struct
 */
@Entity
public class PlaceHour extends AbsAuditData implements HashSetData, ManyEntity<PlaceLocation> {

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

    private PlaceLocation placeLocation;

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

    @Column(nullable = false)
    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
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

    @ManyToOne(fetch = FetchType.LAZY)
    public PlaceLocation getPlaceLocation() {
        return placeLocation;
    }

    public void setPlaceLocation(PlaceLocation placeLocation) {
        this.placeLocation = placeLocation;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return equals(obj, getClass());
    }

    @Override
    public void setOneEntity(PlaceLocation single) {
        setPlaceLocation(single);
    }
}
