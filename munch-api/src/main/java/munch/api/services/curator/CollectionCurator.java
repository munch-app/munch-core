package munch.api.services.curator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.data.LatLng;
import munch.api.data.PlaceCollection;
import munch.api.data.SearchQuery;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 18/6/2017
 * Time: 1:30 AM
 * Project: munch-core
 */
@Singleton
public class CollectionCurator {

    private final ExplicitLocationCurator explicitLocationCurator;
    private final ImplicitLocationCurator implicitLocationCurator;
    private final SingaporeCurator singaporeCurator;

    @Inject
    public CollectionCurator(ExplicitLocationCurator explicitLocationCurator,
                             ImplicitLocationCurator implicitLocationCurator,
                             SingaporeCurator singaporeCurator) {
        this.explicitLocationCurator = explicitLocationCurator;
        this.implicitLocationCurator = implicitLocationCurator;
        this.singaporeCurator = singaporeCurator;
    }


    /**
     * Propagate curating to
     * 1. SingaporeCurator if no (latLng or location)
     * 2. ImplicitLocationCurator if contains only implicit latLng
     * 3. ExplicitLocationCurator if contains explicit query.location
     *
     * @param query  mandatory query in search bar
     * @param latLng nullable latLng (user physical location in latLng)
     * @return Curated List of PlaceCollection
     */
    public List<PlaceCollection> search(SearchQuery query, @Nullable LatLng latLng) {
        if (latLng == null && query.getLocation() == null) {
            // No Location data available
            return singaporeCurator.curate(query, null);
        } else if (latLng != null) {
            // Implicit lat lng available
            return implicitLocationCurator.curate(query, latLng);
        } else {
            // Explicit lat lng available
            return explicitLocationCurator.curate(query, null);
        }
    }
}
