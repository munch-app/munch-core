package munch.api.search.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.restful.core.JsonUtils;
import munch.restful.server.JsonService;

import java.util.HashMap;
import java.util.Map;

/**
 * ElasticInput parser
 */
public final class FilterElasticUtils {

    /**
     * @return agg node
     */
    public static JsonNode agg(boolean prices, boolean tags, boolean count) {
        ObjectNode aggsNode = JsonUtils.createObjectNode();
        if (prices) aggsNode.set("prices", FilterElasticUtils.aggPrices());
        if (tags) aggsNode.set("tags", FilterElasticUtils.aggTags());
        if (count) aggsNode.set("count", FilterElasticUtils.aggCount());

        return aggsNode;
    }

    public static JsonNode aggTags() {
        ObjectNode tag = JsonService.objectMapper.createObjectNode();
        tag.putObject("terms")
                .put("field", "tags.tagId")
                .put("size", 3000);
        return tag;
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-bucket-histogram-aggregation.html
     *
     * @return price bucket
     */
    public static JsonNode aggPrices() {
        ObjectNode priceRange = JsonService.objectMapper.createObjectNode();
        priceRange.putObject("histogram")
                .put("interval", 3)
                .put("field", "price.perPax");
        return priceRange;
    }

    public static JsonNode aggCount() {
        ObjectNode count = JsonService.objectMapper.createObjectNode();
        count.putObject("terms")
                .put("field", "status.type")
                .put("size", 10);
        return count;
    }

    /**
     * @param aggregations result to parse
     * @return tagId > count
     */
    public static Map<String, Integer> parseTags(JsonNode aggregations) {
        Map<String, Integer> counts = new HashMap<>();

        for (JsonNode object : aggregations.path("tags").path("buckets")) {
            String key = object.path("key").asText();
            int count = object.path("doc_count").asInt();
            counts.put(key, count);
        }

        return counts;
    }

    /**
     * @param aggregations result to parse
     * @return priceInterval > count
     */
    public static Map<Double, Integer> parsePrice(JsonNode aggregations) {
        Map<Double, Integer> frequency = new HashMap<>();

        for (JsonNode object : aggregations.path("prices").path("buckets")) {
            double key = object.path("key").asDouble();
            int count = object.path("doc_count").asInt();
            frequency.put(key, count);
        }

        return frequency;
    }

    public static long parseCount(JsonNode aggregations) {
        for (JsonNode object : aggregations.path("count").path("buckets")) {
            if (object.path("key").asText().equals("open")) {
                return object.path("doc_count").asLong();
            }
        }

        return 0;
    }
}
