package munch.api.services.curator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.SearchClient;
import munch.api.services.AbstractService;
import munch.data.Place;
import munch.data.SearchCollection;
import munch.data.SearchQuery;
import munch.data.SearchResult;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 10/7/2017
 * Time: 11:04 PM
 * Project: munch-core
 */
@Singleton
public abstract class TabCurator extends Curator {
    public static final int SEARCH_SIZE = 50;

    private static final int TAB_SIZE = 4;
    private static final int TAB_MIN_RESULT_SIZE = 1;

    @Inject
    protected TabCurator(SearchClient searchClient) {
        super(searchClient);
    }

    /**
     * @param query  mandatory query in search bar
     * @param latLng nullable latLng (user physical location in latLng)
     * @return Result of Place
     */
    public abstract List<SearchResult> query(SearchQuery query, @Nullable AbstractService.LatLng latLng);

    @Override
    public List<SearchCollection> curate(SearchQuery query, @Nullable AbstractService.LatLng latLng) {
        List<SearchResult> results = query(query, latLng);

        List<SearchCollection> collections = new ArrayList<>();
        collections.add(new SearchCollection("HIGHLIGHT", query, results));

        collections.addAll(categorize(query, results, TAB_SIZE, TAB_MIN_RESULT_SIZE));
        return collections;
    }

    /**
     * @param source  search query source
     * @param results results to categorize
     * @param size    size of collections to return
     * @return categorized collection
     */
    protected List<SearchCollection> categorize(SearchQuery source, List<SearchResult> results, int size, int minResults) {
        Map<String, List<SearchResult>> category = new HashMap<>();

        // Add all results into category with key: tag, value: place
        for (SearchResult result : results) {
            if (result instanceof Place) {
                for (String tag : ((Place) result).getTags()) {
                    category.compute(tag.toLowerCase(), (s, list) -> {
                        if (list == null) list = new ArrayList<>();
                        list.add(result);
                        return list;
                    });
                }
            }
        }

        Map<String, List<SearchResult>> sorted = category.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getValue().size()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> {
                            throw new AssertionError();
                        }, LinkedHashMap::new
                ));

        List<SearchCollection> collections = new ArrayList<>();
        for (Map.Entry<String, List<SearchResult>> entry : sorted.entrySet()) {
            // Check that list is not less then min result size
            if (entry.getValue().size() < minResults) break;

            // Add new SearchCollection
            String tag = entry.getKey().toUpperCase();
            SearchQuery query = withTag(source, tag);
            collections.add(new SearchCollection(tag, query, entry.getValue()));

            // Exit if more than or equal size
            if (collections.size() >= size) break;
        }
        return collections;
    }

    /**
     * @param source source of search query to extend
     * @param tags   tag to add
     * @return Cloned and edit SearchQuery with addition of tag
     */
    protected SearchQuery withTag(SearchQuery source, String... tags) {
        SearchQuery query = clone(source);
        query.setFrom(0);
        query.setSize(0);
        if (query.getFilter() == null) query.setFilter(new SearchQuery.Filter());
        if (query.getFilter().getTag() == null) query.getFilter().setTag(new SearchQuery.Filter.Tag());
        if (query.getFilter().getTag().getPositives() == null) query.getFilter().getTag().setPositives(new HashSet<>());

        // Add the tags
        for (String tag : tags) {
            query.getFilter().getTag().getPositives().add(tag);
        }
        return query;
    }
}
