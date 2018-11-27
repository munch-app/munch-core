package munch.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.ApiService;
import munch.api.search.data.FilterResult;
import munch.api.search.elastic.ElasticQueryUtils;
import munch.data.client.ElasticClient;
import munch.restful.core.JsonUtils;
import munch.restful.server.JsonCall;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 15/6/18
 * Time: 10:50 PM
 * Project: munch-core
 */
@Singleton
public final class SearchFilterService extends ApiService {
    private final ElasticClient elasticClient;
    private final SearchRequest.Factory searchRequestFactory;

    @Inject
    public SearchFilterService(ElasticClient elasticClient, SearchRequest.Factory searchRequestFactory) {
        this.elasticClient = elasticClient;
        this.searchRequestFactory = searchRequestFactory;
    }

    @Override
    public void route() {
        PATH("/search/filter", () -> {
            POST("", this::post);
        });
    }

    public FilterResult post(JsonCall call) {
        SearchRequest request = searchRequestFactory.create(call);
        if (request.hasPrice()) {
            JsonNode aggregations = postAggregation(request, false, true, true);
            long count = ElasticOutput.parseCount(aggregations);
            Map<String, Integer> tags = ElasticOutput.parseTagCounts(aggregations);

            request.getSearchQuery().getFilter().setPrice(null);
            aggregations = postAggregation(request, true, false, false);
            Map<Double, Integer> frequency = ElasticOutput.parsePriceFrequency(aggregations);

            return FilterResult.from(count, tags, frequency);
        } else {
            JsonNode aggregations = postAggregation(request, true, true, true);

            long count = ElasticOutput.parseCount(aggregations);
            Map<String, Integer> tags = ElasticOutput.parseTagCounts(aggregations);
            Map<Double, Integer> frequency = ElasticOutput.parsePriceFrequency(aggregations);
            return FilterResult.from(count, tags, frequency);
        }
    }

    /**
     * @param request search request to read from
     * @param prices  whether to agg price
     * @param tags    whether to agg tags
     * @param count   whether to agg count
     * @return aggregation node
     */
    private JsonNode postAggregation(SearchRequest request, boolean prices, boolean tags, boolean count) {
        if (!prices && !tags) throw new IllegalStateException("Either prices or tags must be true.");

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("size", 0);
        rootNode.set("query", ElasticQueryUtils.make(request));

        ObjectNode aggsNode = JsonUtils.createObjectNode();
        if (prices) aggsNode.set("prices", ElasticInput.aggPrices());
        if (tags) aggsNode.set("tags", ElasticInput.aggTags());
        if (count) aggsNode.set("count", ElasticInput.aggCount());
        rootNode.set("aggs", aggsNode);

        JsonNode result = elasticClient.search(rootNode);
        return result.path("aggregations");
    }

    /**
     * ElasticInput parser
     */
    private static final class ElasticInput {
        private static JsonNode aggTags() {
            ObjectNode tag = objectMapper.createObjectNode();
            tag.putObject("terms")
                    .put("field", "tags.name")
                    .put("size", 3000);
            return tag;
        }

        /**
         * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-bucket-histogram-aggregation.html
         *
         * @return price bucket
         */
        private static JsonNode aggPrices() {
            ObjectNode priceRange = objectMapper.createObjectNode();
            priceRange.putObject("histogram")
                    .put("interval", 3)
                    .put("field", "price.perPax");
            return priceRange;
        }

        private static JsonNode aggCount() {
            ObjectNode count = objectMapper.createObjectNode();
            count.putObject("terms")
                    .put("field", "status.type")
                    .put("size", 10);
            return count;
        }
    }

    /**
     * ElasticOutput parser
     */
    private static final class ElasticOutput {
        private static Map<String, Integer> parseTagCounts(JsonNode aggregations) {
            Map<String, Integer> counts = new HashMap<>();

            for (JsonNode object : aggregations.path("tags").path("buckets")) {
                String key = object.path("key").asText();
                int count = object.path("doc_count").asInt();
                counts.put(key, count);
            }

            return counts;
        }

        private static Map<Double, Integer> parsePriceFrequency(JsonNode aggregations) {
            Map<Double, Integer> frequency = new HashMap<>();

            for (JsonNode object : aggregations.path("prices").path("buckets")) {
                double key = object.path("key").asDouble();
                int count = object.path("doc_count").asInt();
                frequency.put(key, count);
            }

            return frequency;
        }

        private static long parseCount(JsonNode aggregations) {
            for (JsonNode object : aggregations.path("count").path("buckets")) {
                if (object.path("key").asText().equals("open")) {
                    return object.path("doc_count").asLong();
                }
            }

            return 0;
        }
    }
}
