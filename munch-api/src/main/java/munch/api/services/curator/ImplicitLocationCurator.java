package munch.api.services.curator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.SearchClient;
import munch.api.data.LatLng;
import munch.api.data.SearchQuery;
import munch.api.data.SearchResult;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 1/7/2017
 * Time: 11:08 PM
 * Project: munch-core
 */
@Singleton
public class ImplicitLocationCurator extends TabCurator {

    @Inject
    protected ImplicitLocationCurator(SearchClient searchClient) {
        super(searchClient);
    }

    @Override
    public List<SearchResult> query(SearchQuery query, LatLng latLng) {
        query.setFrom(0);
        query.setSize(SEARCH_SIZE);
        resolveLocation(query, latLng);
        return searchClient.search(query);
    }

    @Override
    public boolean match(SearchQuery query, @Nullable LatLng latLng) {
        // Non complex query
        if (isComplex(query)) return false;
        // Check Location not Null
        if (query.getLocation() != null) {
            // Points Array not Null
            if (query.getLocation().getPoints() != null) {

                if (query.getLocation().getPoints().size() > 2) return false;
            }
        }

        // Contains implicit location
        return latLng != null;
    }

    /**
     * If LatLng is not null
     * query.getFilter().getDistance is not null
     * Then: Add a filter of distance of latLng and radius of 1000 metres
     *
     * @param query  search query
     * @param latLng user current location
     */
    public static void resolveLocation(SearchQuery query, @Nullable LatLng latLng) {
        // Has no explicit location but has implicit location
        if (latLng != null) {
            if (query.getFilter() == null) query.setFilter(new SearchQuery.Filter());
            if (query.getFilter().getDistance() == null) {
                query.getFilter().setDistance(new SearchQuery.Filter.Distance());
            }

            SearchQuery.Filter.Distance distance = query.getFilter().getDistance();
            distance.setLatLng(latLng);
            // 0 - 1000 Metres of Radius
            distance.setMin(0);
            distance.setMax(1000);
        }
    }
}
