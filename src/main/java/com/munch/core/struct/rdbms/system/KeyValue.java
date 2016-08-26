package com.munch.core.struct.rdbms.system;

import com.munch.core.struct.rdbms.abs.AbsAuditData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created By: Fuxing Loh
 * Date: 23/8/2016
 * Time: 8:50 PM
 * Project: struct
 */
@Entity
public class KeyValue extends AbsAuditData {

    private String key;
    private String value;


    @Id
    @Column(nullable = true, length = 255)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Column(nullable = true, length = 1000)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
