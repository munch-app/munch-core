package com.munch.catalyst;

import com.catalyst.client.GroupPersist;
import com.corpus.object.GroupField;
import com.corpus.object.GroupObject;
import com.corpus.object.ObjectUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.munch.struct.Location;
import com.munch.struct.OpeningHours;
import com.munch.struct.Place;
import com.munch.hibernate.utils.TransactionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 15/2/2017
 * Time: 2:56 AM
 * Project: munch-core
 */
@Singleton
public class MunchGroupPersist implements GroupPersist {
    private static final Logger logger = LoggerFactory.getLogger(MunchGroupPersist.class);

    private final TransactionProvider provider;

    /**
     * @param provider core database provider
     */
    @Inject
    public MunchGroupPersist(@Named("struct") TransactionProvider provider) {
        this.provider = provider;
    }

    /**
     * 1. Persist to core database
     * 2. Persist to elastic search
     * 3. Persist to bucket for images
     * 4. Persist to cache?
     *
     * @param list list to persist
     */
    @Override
    public void persist(List<GroupObject> list) {
        provider.with(em -> {
            for (GroupObject group : list) {
                Place place = em.find(Place.class, group.getGroupKey());
                if (place == null) place = new Place();
                // Merge and persist group
                merge(place, group);
                em.persist(place);
            }
        });
    }

    private void merge(Place place, GroupObject group) {
        // Set basic info
        place.setName(getString(group, "name"));
        place.setPhone(getString(group, "phone"));
        place.setWebsite(getString(group, "website"));
        place.setDescription(getString(group, "description"));

        // Set other entity
        place.setLocation(createLocation(group));
        place.setOpeningHours(createHours(group));

        // Set tracking dates
        place.setCreatedDate(group.getCreatedDate());
        place.setUpdatedDate(group.getUpdatedDate());
    }

    /**
     * @param group group object
     * @return Location created from group
     */
    private Location createLocation(GroupObject group) {
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
    private Set<OpeningHours> createHours(GroupObject group) {
        return Collections.emptySet();
    }

    /**
     * @param group group object
     * @param key   key of field
     * @return string value, return null if value not found
     */
    private String getString(GroupObject group, String key) {
        return ObjectUtils.getField(group, key).map(GroupField::getValue).orElse(null);
    }

    /**
     * @param group group object
     * @param key   key of field
     * @return double value, return null if value not found or parse-able
     */
    private Double getDouble(GroupObject group, String key) {
        String string = getString(group, key);
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            logger.warn("Group: {} getDouble({}) NumberFormatException {} for group: {}", group.getGroupKey(), key, e, group);
            return null;
        }
    }
}
