package munch.api.services.curator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.PlaceClient;
import munch.api.data.LatLng;
import munch.api.data.PlaceCollection;
import munch.api.data.SearchQuery;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Use this curator if there is no Location data at all
 * 1. No polygon
 * 2. No latLng
 * This curator is compatible if there is query or filter data
 * <p>
 * Created by: Fuxing
 * Date: 1/7/2017
 * Time: 10:15 PM
 * Project: munch-core
 */
@Singleton
public class SingaporeCurator extends Curator {

    @Inject
    public SingaporeCurator(PlaceClient placeClient) {
        super(placeClient);
    }

    /**
     * Location data is ignored
     * Singapore curator only respect this values
     * 1. query
     * 2. filters
     *
     * @param query  mandatory query in search bar, polygon will be ignored
     * @param latLng ignored for Singapore curator
     * @return Curated List of PlaceCollection
     */
    @Override
    protected List<PlaceCollection> curate(SearchQuery query, @Nullable LatLng latLng) {
        query.setLocation(null);
        List<PlaceCollection> collections = new ArrayList<>();

        // If not empty do search based on query
        if (!isEmpty(query)) collections.add(new PlaceCollection("SEARCH", query));

        // Preset Default Collections
        collections.add(new PlaceCollection("BREAKFAST", createTagQuery("breakfast")));
        collections.add(new PlaceCollection("HALAL", createTagQuery("halal")));
        collections.add(new PlaceCollection("HEALTHY OPTIONS", createTagQuery("healthy options")));
        return collections;
    }
}
