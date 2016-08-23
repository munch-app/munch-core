package com.munch.core.struct.database.abs;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created By: Fuxing Loh
 * Date: 23/8/2016
 * Time: 6:21 PM
 * Project: struct
 */
@MappedSuperclass
public class AbsSortData extends AbsAuditData {

    private long sort;

    @Column
    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }

}
