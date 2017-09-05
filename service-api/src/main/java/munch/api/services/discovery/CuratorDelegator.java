package munch.api.services.discovery;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.SearchClient;
import munch.data.SearchCollection;
import munch.data.SearchQuery;

import java.util.Collections;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 10/7/2017
 * Time: 11:06 PM
 * Project: munch-core
 */
@Singleton
public final class CuratorDelegator {
    public static final int SEARCH_SIZE = 20;

    private final SearchClient searchClient;
    private final List<Curator> curators;

    @Inject
    public CuratorDelegator(SearchClient searchClient,
                            NonCurator nonCurator,
                            SpecialCurator specialCurator,
                            PolygonCurator polygonCurator) {
        this.searchClient = searchClient;
        this.curators = ImmutableList.of(
                nonCurator,     // With Explicit Search Condition
                specialCurator, // With Special Polygon
                polygonCurator  // With Polygon
        );
    }

    /**
     * This is the public subroutine for curate method
     * This method will pre-fill search results for first collection if not already done so
     *
     * @param query mandatory query in search bar, this method is cloned
     * @return Curated List of PlaceCollection
     */
    public List<SearchCollection> delegate(SearchQuery query) {
        SearchQuery cloned = Curator.clone(query);
        List<SearchCollection> collections = curate(cloned);
        if (collections.isEmpty()) return collections;

        // Pre-fill first collection result
        SearchCollection first = collections.get(0);
        // Only fill if not already filled
        if (first.getResults().isEmpty()) {
            first.getQuery().setFrom(0);
            first.getQuery().setSize(SEARCH_SIZE);
            first.setResults(searchClient.search(collections.get(0).getQuery()));
        }
        return collections;
    }

    /**
     * Propagate curating to curators by:
     * 1. Matching
     * 2. Then search if matched
     *
     * @param query mandatory query in search bar
     * @return Curated List of PlaceCollection
     */
    private List<SearchCollection> curate(SearchQuery query) {
        // Safety Override, for scenario where from, size is not send and is required
        query.setFrom(0);
        query.setSize(15);

        for (Curator curator : curators) {
            // If curator matches: do search
            if (curator.match(query)) {
                return curator.curate(query);
            }
        }
        return Collections.emptyList();
    }
}
