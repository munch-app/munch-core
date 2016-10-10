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

    private String id;

    // Data Tracking
    private int humanVersion = 0;
    private int machineVersion = 0;

    // TODO how isit added, who added it

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

    protected void setHumanVersion(int humanVersion) {
        this.humanVersion = humanVersion;
    }

    @Column(nullable = false)
    public int getMachineVersion() {
        return machineVersion;
    }

    protected void setMachineVersion(int machineVersion) {
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

}
