package com.munch.accounts.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by: Fuxing
 * Date: 7/2/2017
 * Time: 2:30 AM
 * Project: munch-core
 */
@Entity
public final class Country {

    private String iso;
    private String code;
    private String name;

    @Id
    @Column(nullable = false, length = 3, updatable = false)
    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    @Column(nullable = false, length = 3, updatable = false, unique = true)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(nullable = false, length = 80, updatable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
