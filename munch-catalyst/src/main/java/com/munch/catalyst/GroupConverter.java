package com.munch.catalyst;

import com.corpus.object.GroupField;
import com.corpus.object.GroupObject;
import com.corpus.object.ObjectUtils;
import com.munch.struct.Hours;
import com.munch.struct.Location;
import com.munch.struct.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;

/**
 * Created By: Fuxing Loh
 * Date: 9/3/2017
 * Time: 3:42 PM
 * Project: munch-core
 */
public class GroupConverter {
    private static final Logger logger = LoggerFactory.getLogger(MunchPersist.class);

    /**
     * @param group group to create place with
     * @return newly created Place
     */
    public static Place create(GroupObject group) {
        Place place = new Place();
        place.setId(group.getGroupKey());

        // Set basic info
        place.setName(getString(group, "name"));
        place.setPhone(getString(group, "phone"));
        place.setWebsite(getString(group, "website"));
        place.setDescription(getString(group, "description"));

        // Set other entity
        place.setLocation(createLocation(group));
        place.setHours(createHours(group));

        // Set tracking dates
        place.setCreatedDate(group.getCreatedDate());
        place.setUpdatedDate(group.getUpdatedDate());

        return place;
    }

    /**
     * @param group group object
     * @return Location created from group
     */
    public static Location createLocation(GroupObject group) {
        Location location = new Location();
        location.setAddress(getString(group, "Location.address"));
        location.setUnitNumber(getString(group, "Location.unitNumber"));

        location.setCity(getString(group, "Location.city"));
        location.setCountry(getString(group, "Location.country"));

        location.setPostal(getString(group, "Location.postal"));
        location.setLat(getDouble(group, "Location.lat"));
        location.setLng(getDouble(group, "Location.lng"));
        return location;
    }

    /**
     * Create opening hours from group
     *
     * @param group group object
     * @return set of opening hours created
     */
    public static Set<Hours> createHours(GroupObject group) {
        return Collections.emptySet();
    }

    /**
     * @param group group object
     * @param key   key of field
     * @return string value, return null if value not found
     */
    private static String getString(GroupObject group, String key) {
        return ObjectUtils.getField(group, key).map(GroupField::getValue).orElse(null);
    }

    /**
     * @param group group object
     * @param key   key of field
     * @return double value, return null if value not found or parse-able
     */
    private static Double getDouble(GroupObject group, String key) {
        String string = getString(group, key);
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            logger.warn("Group: {} getDouble({}) NumberFormatException {} for group: {}", group.getGroupKey(), key, e, group);
            return null;
        }
    }
}