package munch.api.services.discovery;

import com.google.inject.Singleton;
import munch.api.services.CachedService;
import munch.data.SearchQuery;
import munch.data.SearchResult;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 1/7/2017
 * Time: 11:08 PM
 * Project: munch-core
 */
@Singleton
public final class PolygonCurator extends TabCurator {

    @Inject
    public PolygonCurator(CachedService.StaticJson resource) throws IOException {
        super(5, 1, collectTabTags(resource));
    }

    /**
     * @param resource cached StaticJson resource tool
     * @return Set of Tag that can be Tag curated, all in uppercase
     * @throws IOException IOException
     */
    private static Set<String> collectTabTags(CachedService.StaticJson resource) throws IOException {
        Set<String> tags = new HashSet<>();
        resource.getResource("tab-tags.json").fields().forEachRemaining(entry -> {
            entry.getValue().forEach(tag -> tags.add(tag.asText()));
        });
        return tags;
    }

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
        query.setSize(50);
        return searchClient.search(query);
    }
}
