package munch.api.services.discovery;

import com.google.inject.Singleton;
import munch.data.SearchQuery;
import munch.data.SearchResult;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 1/7/2017
 * Time: 11:08 PM
 * Project: munch-core
 */
@Singleton
public final class PolygonCurator extends TabCurator {

    @Override
    public boolean match(SearchQuery query) {
        // Contains polygonal location data
        if (query.getLocation() != null) {
            if (query.getLocation().getPoints() == null) return false;
            // Must has 3 points
            if (query.getLocation().getPoints().size() >= 3) return true;
        }

        // Else fail
        return false;
    }

    @Override
    public List<SearchResult> query(SearchQuery query) {
        query.setFrom(0);
        query.setSize(SEARCH_SIZE);
        return searchClient.search(query);
    }
}
