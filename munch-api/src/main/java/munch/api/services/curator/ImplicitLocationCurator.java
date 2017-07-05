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
public class ImplicitLocationCurator extends Curator {

    @Inject
    protected ImplicitLocationCurator(PlaceClient placeClient) {
        super(placeClient);
    }

    @Override
    protected List<PlaceCollection> curate(SearchQuery query, LatLng latLng) {
        query = clone(query);
        List<PlaceCollection> collections = new ArrayList<>();
        // If contains search query data
        if (!isEmpty(query)) {
            collections.add(new PlaceCollection("NEARBY SEARCH", query));
        } else {
            collections.add(new PlaceCollection("IMPLICIT NEARBY", query));
        }

        // TODO more logic
        return collections;
    }
}
