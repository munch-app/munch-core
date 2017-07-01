package munch.api.services.curator;

import com.google.inject.Inject;
import com.rits.cloning.Cloner;
import munch.api.clients.PlaceClient;
import munch.api.data.LatLng;
import munch.api.data.PlaceCollection;
import munch.api.data.SearchQuery;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 1/7/2017
 * Time: 10:21 PM
 * Project: munch-core
 */
public abstract class Curator {
    public static final int SEARCH_SIZE = 20;
    private static final Cloner cloner = new Cloner();

    protected final PlaceClient placeClient;

    @Inject
    protected Curator(PlaceClient placeClient) {
        this.placeClient = placeClient;
    }

    /**
     * This is the public subroutine for curate method
     * This method will pre-fill search results for first collection if not already done so
     *
     * @param query  mandatory query in search bar
     * @param latLng nullable latLng (user physical location in latLng)
     * @return Curated List of PlaceCollection
     */
    public List<PlaceCollection> search(SearchQuery query, @Nullable LatLng latLng) {
        List<PlaceCollection> collections = curate(query, latLng);
        if (!collections.isEmpty()) {
            // Pre-fill first collection result
            PlaceCollection first = collections.get(0);
            // Only fill if not already filled
            if (first.getPlaces().isEmpty()) {
                first.getQuery().setFrom(0);
                first.getQuery().setSize(SEARCH_SIZE);
                first.setPlaces(placeClient.search(collections.get(0).getQuery()));
            }
        }
        return collections;
    }

    /**
     * @param query  mandatory query in search bar
     * @param latLng nullable latLng (user physical location in latLng)
     * @return Curated List of PlaceCollection
     */
    protected abstract List<PlaceCollection> curate(SearchQuery query, @Nullable LatLng latLng);

    /**
     * @param tags tags for query
     * @return create SearchQuery with filters tags only
     */
    protected static SearchQuery createTagQuery(String... tags) {
        SearchQuery.Builder builder = SearchQuery.builder();
        for (String tag : tags) {
            builder.tag(tag);
        }
        return builder.build();
    }

    /**
     * Check if a SearchQuery is empty
     * Only filters and query is checked
     * For filters: TODO this feature
     * Implicit user settings filters is considered empty
     *
     * @param query query to check if empty
     * @return true if empty
     */
    protected static boolean isEmpty(SearchQuery query) {
        if (StringUtils.isNotBlank(query.getQuery())) return false;
        if (query.getFilters() == null) return true;

        // All filters must be empty or null
        if (query.getFilters().getRatingsAbove() != null) return false;
        if (query.getFilters().getPriceRange() != null) return false;
        if (!query.getFilters().getHours().isEmpty()) return false;
        if (!query.getFilters().getTags().isEmpty()) return false;

        return true;
    }

    /**
     * @param query query to clone
     * @return deep cloned query
     */
    protected static SearchQuery clone(SearchQuery query) {
        return cloner.deepClone(query);
    }
}
