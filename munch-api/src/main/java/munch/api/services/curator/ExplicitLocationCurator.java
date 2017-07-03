package munch.api.services.curator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.PlaceClient;
import munch.api.data.LatLng;
import munch.api.data.PlaceCollection;
import munch.api.data.SearchQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 1/7/2017
 * Time: 11:08 PM
 * Project: munch-core
 */
@Singleton
public class ExplicitLocationCurator extends Curator {

    @Inject
    protected ExplicitLocationCurator(PlaceClient placeClient) {
        super(placeClient);
    }

    @Override
    protected List<PlaceCollection> curate(SearchQuery query, LatLng latLng) {
        List<PlaceCollection> collections = new ArrayList<>();
        String locationName = query.getLocation().getName();

        // If contains search query data
        if (!isEmpty(query)) {
            collections.add(new PlaceCollection(locationName + " SEARCH", query));
        } else {
            collections.add(new PlaceCollection(locationName, query));
        }

        // TODO more curator logic

        return collections;
    }
}
