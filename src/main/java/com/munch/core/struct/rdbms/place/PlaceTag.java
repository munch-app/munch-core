package com.munch.core.struct.rdbms.place;

import com.munch.core.struct.rdbms.abs.AbsAuditData;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by: Fuxing
 * Date: 24/8/2016
 * Time: 3:29 AM
 * Project: struct
 */
@Entity
public class PlaceTag extends AbsAuditData {

    private String id;
    private String name;
    private int count;

    private int source;
    private String sourceUrl;

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Column
    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    /**
     * Basic Domain Name Only
     */
    @Column(length = 100, nullable = true)
    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
