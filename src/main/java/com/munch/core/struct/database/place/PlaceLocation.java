package com.munch.core.struct.database.place;

import com.munch.core.struct.database.abs.AbsSortData;
import com.munch.core.struct.database.type.Country;

import java.util.Set;

/**
 * Created By: Fuxing Loh
 * Date: 23/8/2016
 * Time: 4:16 PM
 * Project: struct
 */
public class PlaceLocation extends AbsSortData {

    // Basic
    private String id;
    private String name;

    // Details
    private String phoneNumber;
    private Set<BusinessHour> businessHours;

    // Location
    private double lat;
    private double lng;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private Country country;

    // Data Tracking
    private int status;
    private int revision;
    private String source;

    // Backward Accessibility
    private Place place;
}
