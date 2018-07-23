package munch.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.ApiService;
import munch.api.search.data.FilterCount;
import munch.api.search.data.FilterPrice;
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
            POST("/count", this::count);
            POST("/price", this::price);
        });
    }

    public FilterCount count(JsonCall call) {
        SearchRequest request = searchRequestFactory.create(call);
        JsonNode queryNode = ElasticQueryUtils.make(request);

        FilterCount filterCount = new FilterCount();

        // Set Count
        Long count = elasticClient.count(JsonUtils.createObjectNode().set("query", queryNode));
        filterCount.setCount(count == null ? 0 : count);

        // Set Tags Count
        ObjectNode aggsNode = JsonUtils.createObjectNode();
        aggsNode.set("tags", ElasticInput.aggTags());
        JsonNode aggregations = postElasticAggs(queryNode, aggsNode);
        filterCount.setTags(ElasticOutput.parseTagCounts(aggregations.path("tags")));

        return filterCount;
    }

    public FilterPrice price(JsonCall call) {
        SearchRequest request = searchRequestFactory.create(call);

        ObjectNode queryNode = JsonUtils.createObjectNode();
        ArrayNode filter = queryNode.putObject("bool").putArray("filter");
        filter.add(ElasticQueryUtils.filterTerm("dataType", "Place"));
        filter.add(ElasticQueryUtils.filterTerm("status.type", "open"));
        ElasticQueryUtils.filterLocation(request).ifPresent(filter::add);

        ObjectNode aggsNode = JsonUtils.createObjectNode();
        aggsNode.set("prices", ElasticInput.aggPrices());

        JsonNode aggregations = postElasticAggs(queryNode, aggsNode);

        FilterPrice filterPrice = new FilterPrice();
        filterPrice.setFrequency(ElasticOutput.parsePriceFrequency(aggregations.path("prices")));
        return filterPrice;
    }

    private JsonNode postElasticAggs(JsonNode queryNode, JsonNode aggsNode) {
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("size", 0);
        rootNode.set("query", queryNode);
        rootNode.set("aggs", aggsNode);

        JsonNode result = elasticClient.search(rootNode);
        return result.path("aggregations");
    }

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
    }

    private static final class ElasticOutput {
        private static Map<String, Integer> parseTagCounts(JsonNode terms) {
            Map<String, Integer> counts = new HashMap<>();

            for (JsonNode object : terms.path("buckets")) {
                String key = object.path("key").asText();
                int count = object.path("doc_count").asInt();
                counts.put(key, count);
            }

            return counts;
        }

        private static Map<Double, Integer> parsePriceFrequency(JsonNode prices) {
            Map<Double, Integer> frequency = new HashMap<>();

            for (JsonNode object : prices.path("buckets")) {
                double key = object.path("key").asDouble();
                int count = object.path("doc_count").asInt();
                frequency.put(key, count);
            }

            return frequency;
        }
    }
}
