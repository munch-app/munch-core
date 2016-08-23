package com.munch.core.struct.database.place;

import com.munch.core.struct.database.abs.AbsAuditData;
import com.munch.core.struct.database.menu.MenuMedia;
import com.munch.core.struct.database.menu.MenuWebsite;
import com.munch.core.struct.database.type.CuisineType;
import com.munch.core.struct.database.type.MealType;
import com.munch.core.struct.database.type.VenueType;

import java.util.Set;

/**
 * Created By: Fuxing Loh
 * Date: 23/8/2016
 * Time: 7:35 PM
 * Project: struct
 */
public class Place extends AbsAuditData {

    // Basic
    private String id;
    private String name;
    private String description;

    // Informational
    private String phoneNumber;
    private String websiteUrl;
    private Set<CuisineType> cuisineTypes;
    private Set<VenueType> venueTypes;
    private Set<MealType> mealTypes;

    // Details
    private double priceStart;
    private double priceEnd;
    private boolean halal;
    private boolean vegan;

    // Menu
    private Set<MenuWebsite> menuWebsites;
    private Set<MenuMedia> menuMedias;

    // Related
    private Set<PlaceLocation> locations;
    private Set<PlaceMedia> medias;

    // Data Tracking
    private int status;
    private int revision;
    private String source;
}
