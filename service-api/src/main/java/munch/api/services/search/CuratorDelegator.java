package munch.api.services.search;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.SearchClient;
import munch.api.services.search.cards.SearchCollection;
import munch.data.SearchQuery;
import munch.data.SearchResult;
import munch.restful.core.exception.StructuredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 10/7/2017
 * Time: 11:06 PM
 * Project: munch-core
 */
@Singleton
public final class CuratorDelegator {
    private static final Logger logger = LoggerFactory.getLogger(CuratorDelegator.class);
    public static final int SEARCH_SIZE = 20;

    private final SearchClient searchClient;
    private final List<Curator> curators;

    @Inject
    public CuratorDelegator(SearchClient searchClient,
                            SearchCurator searchCurator,
                            LocationCurator locationCurator,
                            SingaporeCurator singaporeCurator) {
        this.searchClient = searchClient;
        this.curators = ImmutableList.of(
                searchCurator,   // With Search Condition
                locationCurator, // With Location, Polygon or LatLng
                singaporeCurator // With No Location
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
        // Safety Override, for scenario where from, size is not send and is required
        query.setFrom(0);
        query.setSize(15);

        SearchQuery cloned = Curator.clone(query);
        List<SearchCollection> collections = curate(cloned);
        if (collections.isEmpty()) return collections;

        // Pre-fill first collection result
        SearchCollection first = collections.get(0);
        // Only fill if not already filled
        if (first.getCards().isEmpty()) {
            first.getQuery().setFrom(0);
            first.getQuery().setSize(SEARCH_SIZE);
            List<SearchResult> results = searchClient.search(collections.get(0).getQuery());
            first.setCards(Curator.parseCards(results));
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
        for (Curator curator : curators) {
            // If curator matches: do search
            if (curator.match(query)) {
                return curator.curate(query);
            }
        }

        // Cannot find any curator that fit the conditions
        logger.error("No curator found for search query: {}", query);
        throw new StructuredException(500, "CuratorException", "No curator found.");
    }
}
