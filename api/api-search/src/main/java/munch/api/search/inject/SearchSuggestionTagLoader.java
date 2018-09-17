package munch.api.search.inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.search.SearchRequest;
import munch.api.search.cards.SearchSuggestedTagCard;
import munch.api.search.elastic.ElasticQueryUtils;
import munch.data.client.ElasticClient;
import munch.data.client.TagClient;
import munch.data.tag.Tag;
import munch.restful.core.JsonUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
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

    private final ElasticClient elasticClient;

    private final Map<String, Tag> tags = new HashMap<>();

    @Inject
    public SearchSuggestionTagLoader(TagClient tagClient, ElasticClient elasticClient) {
        this.elasticClient = elasticClient;

        tagClient.iterator().forEachRemaining(tag -> {
            switch (tag.getType()) {
                case Amenities:
                case Cuisine:
                    this.tags.put(tag.getName().toLowerCase(), tag);

            }
        });
    }

    @Override
    public List<Position> load(Request request) {
        if (!request.isFirstPage()) return List.of();
        if (!request.isFullPage()) return List.of();
        if (!request.isNatural()) return List.of();

        Map<String, Integer> tagMap = aggTags(request.getRequest());
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
        return tags.containsKey(entry.getKey().toLowerCase());
    }

    private Map<String, Integer> aggTags(SearchRequest request) {
        ObjectNode rootNode = JsonUtils.createObjectNode();
        rootNode.put("size", 0);
        rootNode.set("query", ElasticQueryUtils.make(request));

        ObjectNode aggs = rootNode.putObject("aggs");
        aggs.set("tags", aggTags());

        ;
        JsonNode result = elasticClient.search(rootNode);

        return parseTagCounts(result.path("aggregations").path("tags"));
    }

    private static JsonNode aggTags() {
        ObjectNode tag = JsonUtils.createObjectNode();
        tag.putObject("terms")
                .put("field", "tags.name")
                .put("size", 1000);
        return tag;
    }

    private static Map<String, Integer> parseTagCounts(JsonNode terms) {
        Map<String, Integer> counts = new HashMap<>();

        for (JsonNode object : terms.path("buckets")) {
            String key = object.path("key").asText();
            int count = object.path("doc_count").asInt();
            counts.put(key, count);
        }

        return counts;
    }
}
