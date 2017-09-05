package munch.api.services.discovery;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.NominatimClient;
import munch.api.clients.SearchClient;
import munch.data.SearchCollection;
import munch.data.SearchQuery;
import munch.data.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 11/7/2017
 * Time: 11:33 AM
 * Project: munch-core
 */
@Singleton
public class NonCurator extends Curator {

    private final NominatimClient nominatimClient;

    /**
     * For Complex Query
     */
    @Inject
    protected NonCurator(SearchClient searchClient, NominatimClient nominatimClient) {
        super(searchClient);
        this.nominatimClient = nominatimClient;
    }

    /**
     * @param query  mandatory query in search bar
     * @param latLng nullable latLng (user physical location in latLng)
     * @return true if search query contains complex query
     */
    @Override
    public boolean match(SearchQuery query) {
        return isComplex(query);
    }

    @Override
    public List<SearchCollection> curate(SearchQuery query) {
        // Resolve implicit location if need to
        if (hasNoExplicitLocation(query)) {
            ImplicitLocationCurator.resolveLocation(query);
        }
        List<SearchResult> result = searchClient.search(query);
        String street = WordUtils.capitalizeFully(nominatimClient.getStreet(latLng));
        // Wrap result into single collection
        return Collections.singletonList(new SearchCollection(street, query, result));
    }

    /**
     * @param query  search query
     * @param latLng user location in latLng
     * @return true if no explicit location
     */
    private boolean hasNoExplicitLocation(SearchQuery query) {
        if (query.getLocation() == null) return true;
        if (query.getLocation().getPoints() == null) return true;
        // Less than 3 points is implicit location
        return query.getLocation().getPoints().size() < 3;
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
