package com.munch.core.struct.rdbms.locality;

import com.munch.core.struct.rdbms.abs.AbsSortData;
import com.munch.core.struct.rdbms.type.Country;

/**
 * Created By: Fuxing Loh
 * Date: 25/9/2016
 * Time: 11:41 PM
 * Project: struct
 */
public class Neighborhood extends AbsSortData {

    private String id;
    private Integer type;

    // TODO Images

    private String name;
    private String description;
    private String websiteUrl;

    private Double lat;
    private Double lng;

    // TODO Grid in lat lng?

    private String address;

    private String city;
    private String state;

    private Country country;

}
