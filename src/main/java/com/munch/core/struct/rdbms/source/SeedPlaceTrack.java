package com.munch.core.struct.rdbms.source;

import com.munch.core.struct.rdbms.abs.AbsSortData;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created By: Fuxing Loh
 * Date: 26/8/2016
 * Time: 10:15 PM
 * Project: struct
 */
@Entity
public class SeedPlaceTrack extends AbsSortData {

    // Just entered the system
    public static final int STATUS_RAW = 100;

    // Crawler System (Spaghetti)
    public static final int STATUS_LOCATION_OPEN = 3_010;
    public static final int STATUS_PROBE_OPEN = 3_020;
    public static final int STATUS_SOURCE_ENTRY_OPEN = 3_030;
    public static final int STATUS_EXTRACT_OPEN = 3_040;
    public static final int STATUS_REVIEW_OPEN = 3_050;
    public static final int STATUS_ENTRY_OPEN = 3_200;

    // Final Outcome
    public static final int STATUS_COMPLETED = 9_200;
    public static final int STATUS_ALREADY_EXIST = 9_201;
    public static final int STATUS_BRANCH = 9_202;

    public static final int STATUS_DELETED = 9_400;

    // Main
    private String id;
    private String name;
    private String placeId; // Final id after the 9_2xx series

    private int status;
    private int source;
    private String origin; // The guy that uploaded it

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(length = 255, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 32, columnDefinition = "CHAR(32)", nullable = true)
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Column
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column
    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    /**
     * IMPORTANT
     *
     * @return Either user id or describe where it came from!
     */
    @Column(length = 50, nullable = false)
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
