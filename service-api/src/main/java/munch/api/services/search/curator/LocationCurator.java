package munch.api.services.search.curator;

import com.google.inject.Singleton;
import munch.api.clients.StaticJsonResource;
import munch.data.SearchQuery;
import munch.data.SearchResult;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 1/7/2017
 * Time: 11:08 PM
 * Project: munch-core
 */
@Singleton
public final class LocationCurator extends TabCurator {

    @Inject
    public LocationCurator(StaticJsonResource resource) throws IOException {
        super(5, 1, resource.getResource("tags-tab.json", String[].class));
    }

    @Override
    public boolean match(SearchQuery query) {
        if (StringUtils.isNotBlank(query.getLatLng())) return true;

        // Contains polygonal location data
        if (query.getLocation() != null) {
            // Must has 3 points
            if (query.getLocation().getPoints() == null) return false;
            if (query.getLocation().getPoints().size() >= 3) return true;
        }

        // Else fail
        return false;
    }

    @Override
    public List<SearchResult> query(SearchQuery query) {
        query = clone(query);

        query.setFrom(0);
        query.setSize(60);
        return searchClient.search(query);
    }
}
