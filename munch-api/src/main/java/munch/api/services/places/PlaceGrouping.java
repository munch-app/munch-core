package munch.api.services.places;

import com.google.common.collect.ImmutableList;
import com.google.inject.Singleton;
import munch.api.struct.Place;
import munch.api.struct.PlaceCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by: Fuxing
 * Date: 23/4/2017
 * Time: 9:47 AM
 * Project: munch-core
 */
@Singleton
public class PlaceGrouping {
    private static final int GROUP_MIN_SIZE = 5;
    private static final int MIN_GROUPS = 2;

    private static final List<GroupLogic> list = ImmutableList.of(
            new GroupLogic("Healthy Food", null, place -> {
                return place.getTags().contains("healthy options");
            }),
            new GroupLogic("Halal Food", new PlaceCollection.Filter(), place -> {
                return place.getTags().contains("halal");
            })
    );

    /**
     * @param places    places to parse to group
     * @param groupSize average group size
     * @param minGroups min number of group
     * @param maxGroups max number of group
     * @return List of PlaceCollection (Groups)
     */
    public List<PlaceCollection> parse(List<Place> places) {
        List<PlaceCollection> collections = new ArrayList<>();
        for (GroupLogic groupLogic : list) {
            List<Place> group = new ArrayList<>();

            // Test each place in list
            for (Place place : places) {
                if (groupLogic.is(place)) {
                    group.add(place);
                }
            }

            // Check if size can fulfil, else ignore group list
            if (group.size() >= GROUP_MIN_SIZE) {
                collections.add(create(groupLogic.name, groupLogic.filter, group));
            }
        }

        // Check if group hit min conditions
        if (collections.size() >= MIN_GROUPS) {
            // Yes, make sure no duplicate
            for (PlaceCollection collection : collections) {
                places.removeAll(collection.getPlaces());
            }
        }
        // Add all remaining to empty list
        collections.add(empty(places));
        return collections;
    }

    /**
     * @param places places in collection that has no name
     * @return nameless place collection
     */
    public PlaceCollection empty(List<Place> places) {
        return create(null, null, places);
    }

    /**
     * @param name   name of collection
     * @param filter filter that can be applied to search for see more
     * @param places places in collection
     * @return created PlaceCollection
     */
    public PlaceCollection create(String name, PlaceCollection.Filter filter, List<Place> places) {
        PlaceCollection collection = new PlaceCollection();
        collection.setName(name);
        collection.setFilter(filter);
        collection.setPlaces(places);
        return collection;
    }

    /**
     * Group predicate logic
     * And reapplied filters
     */
    private static class GroupLogic {
        private String name;
        private PlaceCollection.Filter filter;
        private Predicate<Place> predicate;

        private GroupLogic(String name, PlaceCollection.Filter filter, Predicate<Place> predicate) {
            this.name = name;
            this.filter = filter;
            this.predicate = predicate;
        }

        private boolean is(Place place) {
            return predicate.test(place);
        }
    }
}
