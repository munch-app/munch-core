package munch.api.services.search;

import munch.data.Place;
import munch.data.search.SearchCard;
import munch.data.search.SearchCollection;
import munch.data.search.SearchQuery;
import munch.data.search.SearchResult;

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
    private final int tabMinResultSize;

    private final Set<String> tags;

    protected TabCurator(int maxTabSize, int tabMinResultSize, Set<String> tags) {
        this.maxTabSize = maxTabSize;
        this.tabMinResultSize = tabMinResultSize;
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
        List<SearchCard> cards = parseCards(results);

        List<SearchCollection> collections = new ArrayList<>();
        collections.add(new SearchCollection("HIGHLIGHT", query, cards));

        collections.addAll(categorize(query, results));
        return collections;
    }

    /**
     * @param source  search query source
     * @param results results to categorize
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
                        if (tags.contains(tag.toUpperCase())) {
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
            if (list.size() < tabMinResultSize) return;

            // Add new SearchCollection
            SearchQuery query = withTag(source, tag);
            collections.add(new SearchCollection(tag, query, parseCards(results)));
        });

        return collections.stream()
                // Most results on top
                .sorted((o1, o2) -> Integer.compare(o2.getCards().size(), o1.getCards().size()))
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
