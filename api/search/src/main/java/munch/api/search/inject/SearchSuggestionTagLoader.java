package munch.api.search.inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import munch.api.search.FilterManager;
import munch.api.search.SearchManager;
import munch.api.search.cards.SearchSuggestedTagCard;
import munch.api.search.data.SearchQuery;
import munch.api.search.elastic.BoolQuery;
import munch.data.client.ElasticClient;
import munch.data.tag.Tag;
import munch.restful.core.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 12/5/18
 * Time: 2:37 AM
 * Project: munch-core
 */
@Singleton
public final class SearchSuggestionTagLoader implements SearchCardInjector.Loader {

    private final BoolQuery boolQuery;
    private final ElasticClient elasticClient;

    private final Supplier<Map<String, Tag>> supplier;

    @Inject
    public SearchSuggestionTagLoader(BoolQuery boolQuery, ElasticIndex elasticIndex, ElasticClient elasticClient) {
        this.boolQuery = boolQuery;
        this.elasticClient = elasticClient;

        supplier = Suppliers.memoize(() -> {
            Iterator<Tag> iterator = elasticIndex.scroll("Tag", "2m");

            Map<String, Tag> map = new HashMap<>();
            iterator.forEachRemaining(tag -> {
                if (StringUtils.containsAny(tag.getType().toLowerCase(), "amenities", "occasion", "cuisine")) {
                    map.put(tag.getName().toLowerCase(), tag);
                }
            });
            return map;
        });
        supplier.get();
    }

    @Override
    public List<Position> load(Request request) {
        if (!request.isCardsMore(25)) return List.of();
        if (!request.isNatural()) return List.of();

        Map<String, Integer> tagMap = aggTags(request.getQuery());
        if (tagMap.isEmpty()) return List.of();

        List<SearchSuggestedTagCard.Tag> tags = tagMap.entrySet().stream()
                .filter(this::filter)
                .sorted((o1, o2) -> Integer.compare(o2.getValue(), o1.getValue()))
                .map(e -> new SearchSuggestedTagCard.Tag(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        if (tags.size() < 4) return List.of();

        // Only return if more then 3
        SearchSuggestedTagCard card = new SearchSuggestedTagCard();
        card.setLocationName(request.getLocationName(null));
        card.setTags(tags);
        return of(20, card);
    }

    private boolean filter(Map.Entry<String, Integer> entry) {
        if (entry.getValue() < 10) return false;
        return supplier.get().containsKey(entry.getKey().toLowerCase());
    }

    private Map<String, Integer> aggTags(SearchQuery query) {
        query.setRadius(SearchManager.resolveRadius(query));

        ObjectNode rootNode = JsonUtils.createObjectNode();
        rootNode.put("size", 0);
        rootNode.putObject("query").set("bool", boolQuery.make(query));
        ObjectNode aggs = rootNode.putObject("aggs");
        aggs.set("tags", FilterManager.aggTags());

        JsonNode result = elasticClient.postSearch(rootNode);

        return FilterManager.parseTag(result.path("aggregations").path("tags"));
    }
}
