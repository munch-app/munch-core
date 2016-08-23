package com.munch.core.struct.database.place;

import com.munch.core.struct.database.abs.AbsSortData;

import javax.persistence.*;

/**
 * Created by: Fuxing
 * Date: 24/8/2016
 * Time: 1:08 AM
 * Project: struct
 */
@Entity
public class PlaceType extends AbsSortData {

    public static final int MEAL = 3;
    public static final int VENUE = 4;
    public static final int CUISINE = 5;
    public static final int OTHER = 10;

    private long id;
    private String name;

    private int type;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    /**
     * Allow creating and setting id but not recommended
     *
     * @param id long id
     */
    protected void setId(long id) {
        this.id = id;
    }

    @Column
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return Name (Max length of 150)
     */
    @Column(nullable = false, length = 80)
    public String getName() {
        return name;
    }

    /**
     * @param name String name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }
}
