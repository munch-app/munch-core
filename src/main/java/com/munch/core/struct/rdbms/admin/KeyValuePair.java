package com.munch.core.struct.rdbms.admin;

import com.munch.core.struct.rdbms.abs.AbsAuditData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Should use nosql for this kind of data
 * <p>
 * key length = 255
 * value length = 4000
 * <p>
 * Created By: Fuxing Loh
 * Date: 23/8/2016
 * Time: 8:50 PM
 * Project: struct
 */
@Entity
public class KeyValuePair extends AbsAuditData {

    private String keyId;
    private String value;

    @Id
    @Column(nullable = true, length = 255)
    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String key) {
        this.keyId = key;
    }

    @Column(nullable = true, length = 10000)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
