package com.munch.core.struct.rdbms.place;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * All Related tracking and status data is moved to this class as this data can be confusing
 * <p>
 * Created by: Fuxing
 * Date: 11/10/2016
 * Time: 3:01 AM
 * Project: struct
 */
@Entity
public class PlaceLog {

    public static final int STATUS_ACTIVE = 200;
    public static final int STATUS_DELETED = 400;

    public static final int HOW_SPAGHETTI_FEEDFORWARD = 100;
    public static final int HOW_SPAGHETTI_USER = 120;

    public static final int HOW_DASHBOARD_RESTAURANT = 200;
    public static final int HOW_CORPUS_INSTAGRAM = 300;
    public static final int HOW_CORPUS_BLOG = 400;

    private String id;

    // Data Tracking
    private int humanVersion = 0;
    private int machineVersion = 0;

    private Integer addedHow;
    private String addedBy; // User Id

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
    public int getHumanVersion() {
        return humanVersion;
    }

    public void setHumanVersion(int humanVersion) {
        this.humanVersion = humanVersion;
    }

    @Column(nullable = false)
    public int getMachineVersion() {
        return machineVersion;
    }

    public void setMachineVersion(int machineVersion) {
        this.machineVersion = machineVersion;
    }

    @Transient
    public void incrementHumanVersion() {
        setHumanVersion(humanVersion++);
        incrementMachineVersion();
    }

    @Transient
    public void incrementMachineVersion() {
        setMachineVersion(machineVersion++);
    }

    @Column(nullable = false)
    public Integer getAddedHow() {
        return addedHow;
    }

    public void setAddedHow(Integer addedHow) {
        this.addedHow = addedHow;
    }

    @Column(length = 50, nullable = true)
    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }
}
