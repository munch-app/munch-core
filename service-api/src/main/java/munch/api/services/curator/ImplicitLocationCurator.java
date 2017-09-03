package munch.api.services.curator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.SearchClient;
import munch.api.services.AbstractService;
import munch.data.SearchQuery;
import munch.data.SearchResult;

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
    public List<SearchResult> query(SearchQuery query, AbstractService.LatLng latLng) {
        query.setFrom(0);
        query.setSize(SEARCH_SIZE);
        resolveLocation(query, latLng);
        return searchClient.search(query);
    }

    @Override
    public boolean match(SearchQuery query, @Nullable AbstractService.LatLng latLng) {
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
    public static void resolveLocation(SearchQuery query, @Nullable AbstractService.LatLng latLng) {
        if (query.getSort() == null) query.setSort(new SearchQuery.Sort());
        if (query.getFilter() == null) query.setFilter(new SearchQuery.Filter());

        // TODO add polygon of a place first then decide what to use
        // Change MunchApi
    }
}
