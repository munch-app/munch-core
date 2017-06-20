package munch.api.services;

import com.google.inject.Singleton;
import munch.api.data.Place;
import munch.api.data.PlaceCollection;
import munch.api.data.SearchQuery;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 18/6/2017
 * Time: 1:30 AM
 * Project: munch-core
 */
@Singleton
public class PlaceCategorizer {
    private static final int MIN = 5;

    private static final Predicate<Place> CafePredicate = p -> p.getTags().contains("cafe");
    private static final Predicate<Place> HalalPredicate = p -> p.getTags().contains("halal");
    private static final Predicate<Place> HealthyPredicate = p -> p.getTags().contains("healthy options");

    /**
     * Currently supported:
     * 1. Nearby = everything
     * 2. Cafe = tag
     * 3. Halal = tag
     * 4. Healthy = tag
     *
     * @param query  search query
     * @param places places to categorize
     * @return List of PlaceCollection
     */
    public List<PlaceCollection> categorize(SearchQuery query, List<Place> places) {
        List<PlaceCollection> collections = new ArrayList<>();
        // Mandatory collection
        collections.add(group("Nearby", query, places, p -> true));

        // Dynamic collection
        collections.add(group("Cafe", makeQuery(query, "cafe"), places, CafePredicate));
        collections.add(group("Halal", makeQuery(query, "halal"), places, HalalPredicate));
        collections.add(group("Healthy", makeQuery(query, "healthy options"), places, HealthyPredicate));

        collections.removeIf(Objects::isNull);
        return collections;
    }

    /**
     * @param name      name of collection
     * @param query     query for collection
     * @param places    places to filter
     * @param predicate predicate for filter
     * @return PlaceCollection, null if min places not reached
     */
    @Nullable
    private static PlaceCollection group(String name, SearchQuery query, List<Place> places, Predicate<Place> predicate) {
        PlaceCollection collection = new PlaceCollection();
        collection.setName(name);
        collection.setQuery(query);

        List<Place> filtered = places.stream().filter(predicate).collect(Collectors.toList());
        if (filtered.size() < MIN) return null;
        collection.setPlaces(filtered);
        return collection;
    }

    /**
     * @param original original query
     * @return custom SearchQuery
     */
    private static SearchQuery makeQuery(SearchQuery original, String tag) {
        SearchQuery query = new SearchQuery();
        query.setFrom(0);
        query.setSize(20);

        query.setDistance(original.getDistance());
        query.setPolygon(original.getPolygon());

        SearchQuery.Filters filters = new SearchQuery.Filters();
        filters.setTags(Collections.singleton(new SearchQuery.Filters.Tag(tag, true)));
        query.setFilters(filters);
        return query;
    }
}
