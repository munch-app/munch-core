package com.munch.core.struct.database.abs;


import javax.persistence.*;

/**
 * Suggest to add through script and have fixed value, use auto migration for this.
 *
 * Created by: Fuxing
 * Date: 5/2/2016
 * Time: 8:20 AM
 * Project: vital-core
 */
@MappedSuperclass
public abstract class AbsIdNameData extends AbsSortData {

    private long id;
    private String name;

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

    /**
     * @return Name (Max length of 150)
     */
    @Column(nullable = false, length = 150)
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
