package com.munch.core.struct.data.abs;


import javax.persistence.*;

/**
 * Created by: Fuxing
 * Date: 5/2/2016
 * Time: 8:20 AM
 * Project: vital-core
 */
@MappedSuperclass
public abstract class AbsIdNameData extends AbsAuditData {

    /**
     * Suggest to add through script and have fixed value, use auto migration for this.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Max length of 150
     */
    @Column(nullable = false, length = 150)
    private String name;

    @Column
    private int sort;

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

    public String getName() {
        return name;
    }

    /**
     * Allow creating and setting id but not recommended
     *
     * @param name String name
     */
    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int order) {
        this.sort = order;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }
}
