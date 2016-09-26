package com.munch.core.struct.rdbms.locality;

import com.munch.core.struct.rdbms.type.Country;

/**
 * Created By: Fuxing Loh
 * Date: 25/9/2016
 * Time: 11:40 PM
 * Project: struct
 */
public class Container {

    public static final int TYPE_MALL = 1350;
    public static final int TYPE_HAWKER = 1360;

    private String id;
    private Integer type;

    // TODO Images

    private String name;
    private String description;
    private String websiteUrl;

    private Double lat;
    private Double lng;

    // TODO Grid in lat lng? in the future

    private String address;

    private String block;
    private String town;
    private String street;
    private String zipCode;

    private String city;
    private String state;

    private Country country;
    private Neighborhood neighborhood;
}
