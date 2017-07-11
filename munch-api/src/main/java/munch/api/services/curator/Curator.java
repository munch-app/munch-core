package munch.api.services.curator;

import com.google.inject.Inject;
import com.rits.cloning.Cloner;
import munch.api.clients.SearchClient;
import munch.api.data.LatLng;
import munch.api.data.SearchCollection;
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
    protected static final Cloner cloner = new Cloner();

    protected final SearchClient searchClient;

    @Inject
    protected Curator(SearchClient searchClient) {
        this.searchClient = searchClient;
    }

    /**
     * @param query  mandatory query in search bar
     * @param latLng nullable latLng (user physical location in latLng)
     * @return true if current curator can return results
     */
    public abstract boolean match(SearchQuery query, @Nullable LatLng latLng);

    /**
     * @param query  mandatory query in search bar
     * @param latLng nullable latLng (user physical location in latLng)
     * @return Curated List of PlaceCollection
     */
    public abstract List<SearchCollection> curate(SearchQuery query, @Nullable LatLng latLng);

    /**
     * @param query query to clone
     * @return deep cloned query
     */
    protected static SearchQuery clone(SearchQuery query) {
        return cloner.deepClone(query);
    }

    /**
     * Check if a SearchQuery is complex
     * Only filters and query is checked
     * For filters:
     * Implicit user settings filters is considered non complex
     *
     * @param query query to check if empty
     * @return true if complex
     */
    public static boolean isComplex(SearchQuery query) {
        if (StringUtils.isNotBlank(query.getQuery())) return true;
        if (query.getFilter() == null) return false;

        // All filters must be empty or null
        if (query.getFilter().getPrice() != null) {
            if (query.getFilter().getPrice().getMin() != null) return true;
            if (query.getFilter().getPrice().getMax() != null) return true;
        }

        if (query.getFilter().getTag() != null) {
            if (query.getFilter().getTag().getPositives() != null) {
                if (!query.getFilter().getTag().getPositives().isEmpty()) return true;
            }

            if (query.getFilter().getTag().getNegatives() != null) {
                if (!query.getFilter().getTag().getNegatives().isEmpty()) return true;
            }
        }

        if (query.getFilter().getRating() != null) {
            if (query.getFilter().getRating().getMin() != null) return true;
        }

        if (query.getFilter().getDistance() != null) {
            if (query.getFilter().getDistance().getLatLng() != null) return true;
        }

        return false;
    }
}
