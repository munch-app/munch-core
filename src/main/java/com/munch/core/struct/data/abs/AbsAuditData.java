/*
 * Copyright (c) 2015. This file is part of 3LINES PTE. LTD. property that is used for project Puffin which is not to be released for personal use.
 * Failing to do so will result in heavy penalty. However, if this individual file might need to be taken out of context for other purposes within the company,
 * please do ask for permission.
 */

package com.munch.core.struct.data.abs;

import com.munch.core.essential.util.DateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Fuxing
 * Date: 1/1/2015
 * Time: 5:33 PM
 * Project: PuffinCore
 */
@MappedSuperclass
public abstract class AbsAuditData {

    @Column(nullable = false)
    private Date createdDate;

    @Column(nullable = false)
    private Date updatedDate;

    /**
     * Default pre persist time stamping
     */
    @PrePersist
    protected void onCreate() {
        setCreatedDate(new Timestamp(DateTime.millisNow()));
        setUpdatedDate(new Timestamp(DateTime.millisNow()));
    }

    /**
     * Default pre update time stamping
     */
    @PreUpdate
    protected void onUpdate() {
        setUpdatedDate(new Timestamp(DateTime.millisNow()));
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    protected void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    protected void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
