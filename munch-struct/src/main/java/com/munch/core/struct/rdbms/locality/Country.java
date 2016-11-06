package com.munch.core.struct.rdbms.locality;

import com.munch.core.struct.rdbms.abs.AbsSortData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created By: Fuxing Loh
 * Date: 23/8/2016
 * Time: 5:07 PM
 * Project: struct
 */
@Entity
public class Country extends AbsSortData {

    private long id;
    private String name;
    private String code;
    private String iso;

    @Id
    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }

    @Column(nullable = false, length = 80)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false, length = 3, unique = true)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(nullable = false, length = 3, unique = true)
    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }
}
