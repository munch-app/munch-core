package munch.api.services.curator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.SearchClient;
import munch.api.data.LatLng;
import munch.api.data.Place;
import munch.api.data.SearchQuery;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 1/7/2017
 * Time: 11:08 PM
 * Project: munch-core
 */
@Singleton
public class ExplicitLocationCurator extends TabCurator {

    @Inject
    protected ExplicitLocationCurator(SearchClient searchClient) {
        super(searchClient);
    }

    @Override
    public List<Place> query(SearchQuery query, @Nullable LatLng latLng) {
        query = clone(query);
        // TODO search & its size
        return null;
    }

    @Override
    public boolean match(SearchQuery query, @Nullable LatLng latLng) {
        if (isComplex(query)) return false;

        // Contains polygonal location data
        if (query.getLocation() != null) {
            if (query.getLocation().getPoints() == null) return false;
            // Must has 3 points
            if (query.getLocation().getPoints().size() >= 3) return true;
        }

        // Else fail
        return false;
    }
}
