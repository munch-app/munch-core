package munch.api.search.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.search.SearchRequest;
import munch.api.search.cards.SearchSuggestedTagCard;
import munch.api.search.elastic.ElasticQueryUtils;
import munch.api.search.filter.FilterTag;
import munch.api.search.filter.FilterTagDatabase;
import munch.data.client.ElasticClient;
import munch.data.client.TagClient;
import munch.data.tag.Tag;
import munch.restful.core.JsonUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 12/5/18
 * Time: 2:37 AM
 * Project: munch-core
 */
@Singleton
public final class SearchSuggestionTagLoader implements SearchCardPlugin {

    private final ElasticClient elasticClient;
    private final FilterTagDatabase tagDatabase;

    private final Map<String, Tag> tags = new HashMap<>();

    @Inject
    public SearchSuggestionTagLoader(TagClient tagClient, ElasticClient elasticClient, FilterTagDatabase tagDatabase) {
        this.elasticClient = elasticClient;
        this.tagDatabase = tagDatabase;

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
        if (request.getRequest().isBetween()) return List.of();

        Map<String, Integer> tagMap = aggTags(request.getRequest());
        if (tagMap.isEmpty()) return List.of();

        List<FilterTag> tags = tagMap.entrySet().stream()
                .filter(this::filter)
                .sorted((o1, o2) -> Integer.compare(o2.getValue(), o1.getValue()))
                .map(e -> tagDatabase.getTag(e.getKey(), e.getValue()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (tags.size() < 4) return List.of();

        // Only return if more then 3
        String locationName = request.getRequest().getLocationName(null);
        SearchSuggestedTagCard card = new SearchSuggestedTagCard(locationName, tags);
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

        JsonNode result = elasticClient.search(rootNode);

        return parseTagCounts(result.path("aggregations").path("tags"));
    }

    private static JsonNode aggTags() {
        ObjectNode tag = JsonUtils.createObjectNode();
        tag.putObject("terms")
                .put("field", "tags.tagId")
                .put("size", 200);
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
