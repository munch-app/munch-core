package munch.api.services.curator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.PlaceClient;
import munch.api.data.LatLng;
import munch.api.data.Location;
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
    // TODO all the points
    private static final String[] POINTS_BISHAN = new String[]{};
    private static final String[] POINTS_ONE_NORTH = new String[]{};
    private static final String[] POINTS_THOMSON = new String[]{};
    private static final String[] POINTS_SERANGOON_GARDENS = new String[]{};

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
        List<PlaceCollection> collections = new ArrayList<>();

        if (isEmpty(query)) {
            // If query is empty: means location collections
            collections.add(locationQuery("BISHAN", query, POINTS_BISHAN));
            collections.add(locationQuery("ONE NORTH", query, POINTS_ONE_NORTH));
            collections.add(locationQuery("THOMSON", query, POINTS_THOMSON));
            collections.add(locationQuery("SERANGOON GARDENS", query, POINTS_SERANGOON_GARDENS));
        } else {
            // Else do single collection search
            query.setFrom(0);
            query.setSize(15);
            collections.add(new PlaceCollection(null, query, placeClient.search(query)));
        }
        return collections;
    }

    /**
     * Create PlaceLocation with no place data populated
     *
     * @param name   name collection name
     * @param source source of query that will be cloned
     * @param points points for polygon
     * @return collection created
     */
    protected static PlaceCollection locationQuery(String name, SearchQuery source, String[] points) {
        SearchQuery query = clone(source);
        query.setLocation(new Location());
        query.getLocation().setPoints(points);
        return new PlaceCollection(name, query);
    }
}
