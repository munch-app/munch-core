package com.munch.core.struct.rdbms.locality;

import com.munch.core.struct.rdbms.abs.AbsSortData;
import com.munch.core.struct.rdbms.type.Country;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created By: Fuxing Loh
 * Date: 25/9/2016
 * Time: 11:41 PM
 * Project: struct
 */
@Entity
public class Neighborhood extends AbsSortData {

    private String id;
    private Integer type;

    private String name;
    private String description;
    private String websiteUrl;

    private Double lat;
    private Double lng;


    private String address;

    private String city;
    private String state;

    private Country country;

    // TODO Grid in lat lng? in the future

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

}
