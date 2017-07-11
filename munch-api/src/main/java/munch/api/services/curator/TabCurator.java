package munch.api.services.curator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.SearchClient;
import munch.api.data.LatLng;
import munch.api.data.Place;
import munch.api.data.SearchCollection;
import munch.api.data.SearchQuery;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 10/7/2017
 * Time: 11:04 PM
 * Project: munch-core
 */
@Singleton
public abstract class TabCurator extends Curator {

    @Inject
    protected TabCurator(SearchClient searchClient) {
        super(searchClient);
    }

    /**
     * @param query  mandatory query in search bar
     * @param latLng nullable latLng (user physical location in latLng)
     * @return Result of Place
     */
    public abstract List<Place> query(SearchQuery query, @Nullable LatLng latLng);

    @Override
    public List<SearchCollection> curate(SearchQuery query, @Nullable LatLng latLng) {
        // TODO: Curate into tabs
        return null;
    }
}
