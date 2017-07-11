package munch.api.services.curator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.SearchClient;
import munch.api.data.LatLng;
import munch.api.data.SearchCollection;
import munch.api.data.SearchQuery;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 11/7/2017
 * Time: 11:33 AM
 * Project: munch-core
 */
@Singleton
public class NonTabCurator extends Curator {

    @Inject
    protected NonTabCurator(SearchClient searchClient) {
        super(searchClient);
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
        // TODO search
        return null;
    }
}
