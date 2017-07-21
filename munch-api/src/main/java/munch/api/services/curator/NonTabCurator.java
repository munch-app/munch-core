package munch.api.services.curator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.NominatimClient;
import munch.api.clients.SearchClient;
import munch.api.data.LatLng;
import munch.api.data.SearchCollection;
import munch.api.data.SearchQuery;
import munch.api.data.SearchResult;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 11/7/2017
 * Time: 11:33 AM
 * Project: munch-core
 */
@Singleton
public class NonTabCurator extends Curator {

    private final NominatimClient nominatimClient;

    /**
     * For Complex Query
     */
    @Inject
    protected NonTabCurator(SearchClient searchClient, NominatimClient nominatimClient) {
        super(searchClient);
        this.nominatimClient = nominatimClient;
    }

    /**
     * @param query  mandatory query in search bar
     * @param latLng nullable latLng (user physical location in latLng)
     * @return true if search query contains complex query
     */
    @Override
    public boolean match(SearchQuery query, @Nullable LatLng latLng) {
        return isComplex(query);
    }

    @Override
    public List<SearchCollection> curate(SearchQuery query, @Nullable LatLng latLng) {
        // Resolve implicit location if need to
        if (hasNoExplicitLocation(query, latLng)) {
            ImplicitLocationCurator.resolveLocation(query, latLng);
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
    private boolean hasNoExplicitLocation(SearchQuery query, @Nullable LatLng latLng) {
        if (query.getLocation() == null) return true;
        if (query.getLocation().getPoints() == null) return true;
        // Less than 3 points is implicit location
        return query.getLocation().getPoints().size() < 3;
    }
}
