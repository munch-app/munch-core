package munch.api.services.curator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.SearchClient;
import munch.api.data.LatLng;
import munch.api.data.SearchCollection;
import munch.api.data.SearchQuery;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
    private final Set<Curator> curators;

    @Inject
    public CuratorDelegator(SearchClient searchClient, Set<Curator> curators) {
        this.searchClient = searchClient;
        this.curators = curators;
    }

    /**
     * Propagate curating to curators by:
     * 1. Matching
     * 2. Then search if matched
     *
     * @param query  mandatory query in search bar
     * @param latLng nullable latLng (user physical location in latLng)
     * @return Curated List of PlaceCollection
     * @see CuratorModule
     */
    private List<SearchCollection> curate(SearchQuery query, @Nullable LatLng latLng) {
        query.setFrom(0);
        query.setSize(15);

        for (Curator curator : curators) {
            // If curator matches: do search
            if (curator.match(query, latLng)) {
                return curator.curate(query, latLng);
            }
        }
        return Collections.emptyList();
    }

    /**
     * This is the public subroutine for curate method
     * This method will pre-fill search results for first collection if not already done so
     *
     * @param query  mandatory query in search bar, this method is cloned
     * @param latLng nullable latLng (user physical location in latLng)
     * @return Curated List of PlaceCollection
     */
    public List<SearchCollection> delegate(SearchQuery query, @Nullable LatLng latLng) {
        SearchQuery cloned = Curator.clone(query);
        List<SearchCollection> collections = curate(cloned, latLng);
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
}
