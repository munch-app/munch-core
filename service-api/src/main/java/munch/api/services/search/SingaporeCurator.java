package munch.api.services.search;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.services.CachedService;
import munch.data.Location;
import munch.data.search.SearchCollection;
import munch.data.search.SearchQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Use this curator if there is no Location data at all
 * 1. No polygon
 * 2. No latLng
 * This curator is compatible if there is query or filter data
 * <p>
 * Created by: Fuxing
 * Date: 1/7/2017
 * Time: 10:15 PM
 * Project: munch-core
 */
@Singleton
public final class SingaporeCurator extends Curator {

    private final Location[] popularLocations;

    @Inject
    public SingaporeCurator(CachedService.StaticJson resource) throws IOException {
        this.popularLocations = resource.getResource("popular-locations.json", Location[].class);
    }

    @Override
    public boolean match(SearchQuery query) {
        return true;
    }

    /**
     * Location data is ignored
     * Singapore curator only respect this values
     * 1. query
     * 2. filters
     *
     * @param source mandatory query in search bar, polygon will be ignored
     * @return Curated List of PlaceCollection
     */
    @Override
    public List<SearchCollection> curate(SearchQuery source) {
        List<SearchCollection> collections = new ArrayList<>();

        // If query is empty: means location collections
        for (Location location : popularLocations) {
            SearchQuery query = clone(source);
            query.setLocation(location);
            collections.add(new SearchCollection(location.getName().toUpperCase(), query));
        }
        return collections;
    }
}
