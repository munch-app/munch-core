package munch.api.services.discovery;

import munch.data.Place;
import munch.data.SearchCollection;
import munch.data.SearchQuery;
import munch.data.SearchResult;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 10/7/2017
 * Time: 11:04 PM
 * Project: munch-core
 */
public abstract class TabCurator extends Curator {

    private final int maxTabSize;
    private final int minTabResultSize;

    private final Set<String> tags;

    protected TabCurator(int maxTabSize, int minTabResultSize, Set<String> tags) {
        this.maxTabSize = maxTabSize;
        this.minTabResultSize = minTabResultSize;
        this.tags = tags.stream().map(String::toUpperCase).collect(Collectors.toSet());
    }

    /**
     * @param query mandatory query in search bar
     * @return Result of Place
     */
    public abstract List<SearchResult> query(SearchQuery query);

    @Override
    public List<SearchCollection> curate(SearchQuery query) {
        List<SearchResult> results = query(query);

        List<SearchCollection> collections = new ArrayList<>();
        collections.add(new SearchCollection("HIGHLIGHT", query, results));

        collections.addAll(categorize(query, results));
        return collections;
    }

    /**
     * @param source  search query source
     * @param results results to categorize
     * @param size    size of collections to return
     * @return categorized collection
     */
    protected List<SearchCollection> categorize(SearchQuery source, List<SearchResult> results) {
        Map<String, List<SearchResult>> category = new HashMap<>();

        // Add all results into category with key: tag, value: place
        results.stream()
                .filter(result -> result instanceof Place)
                .map(result -> (Place) result)
                .forEach(place -> {
                    for (String tag : place.getTags()) {
                        // Filter to approved tags
                        if (tags.contains(tag)) {
                            // Add to category if found
                            category.compute(tag.toUpperCase(), (s, list) -> {
                                if (list == null) list = new ArrayList<>();
                                list.add(place);
                                return list;
                            });
                        }
                    }
                });

        List<SearchCollection> collections = new ArrayList<>();
        category.forEach((tag, list) -> {
            // Check that list is not less then min result size
            if (list.size() < minTabResultSize) return;

            // Add new SearchCollection
            SearchQuery query = withTag(source, tag);
            collections.add(new SearchCollection(tag, query, list));
        });

        return collections.stream()
                // Most results on top
                .sorted((o1, o2) -> Integer.compare(o2.getResults().size(), o1.getResults().size()))
                // Limit to tab size - 1
                .limit(maxTabSize - 1)
                .collect(Collectors.toList());
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
