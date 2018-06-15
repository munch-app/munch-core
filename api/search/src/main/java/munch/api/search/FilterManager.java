package munch.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.search.data.FilterCount;
import munch.api.search.data.FilterPriceRange;
import munch.api.search.data.SearchQuery;
import munch.api.search.elastic.BoolQuery;
import munch.data.client.ElasticClient;
import munch.data.client.PlaceClient;
import munch.restful.core.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 3/4/2018
 * Time: 4:11 AM
 * Project: munch-core
 */
@Singleton
public class FilterManager {
    private static final Logger logger = LoggerFactory.getLogger(FilterManager.class);
    private static final ObjectMapper objectMapper = JsonUtils.objectMapper;

    private final BoolQuery boolQuery;
    private final PlaceClient placeClient;
    private final ElasticClient elasticClient;

    @Inject
    public FilterManager(BoolQuery boolQuery, PlaceClient placeClient, ElasticClient elasticClient) {
        this.boolQuery = boolQuery;
        this.placeClient = placeClient;
        this.elasticClient = elasticClient;
    }

    public FilterCount filterCount(SearchQuery query) {
        query.setRadius(SearchManager.resolveRadius(query));

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("size", 0);
        rootNode.putObject("query").set("bool", boolQuery.make(query));
        ObjectNode aggs = rootNode.putObject("aggs");
        aggs.set("tags", aggTags());

        JsonNode result = elasticClient.postSearch(rootNode);

        FilterCount filterCount = new FilterCount();
        filterCount.setCount(placeClient.getSearchClient().count(query));
        filterCount.setTags(parseTag(result.path("aggregations").path("tags")));
        return filterCount;
    }

    public FilterPriceRange filterPriceRange(SearchQuery query) {
        query.setRadius(SearchManager.resolveRadius(query));


        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("size", 0);

        ArrayNode filter = rootNode.putObject("query")
                .putObject("bool")
                .putArray("filter");
        filter.add(BoolQuery.filterTerm("dataType", "Place"));
        filter.add(BoolQuery.filterTerm("open", true));
        BoolQuery.filterLocation(query).ifPresent(filter::add);


        ObjectNode aggs = rootNode.putObject("aggs");
        aggs.set("prices", aggPrices());
        aggs.set("price_range", aggPriceRange());

        JsonNode result = elasticClient.postSearch(rootNode);

        FilterPriceRange priceRange = parsePriceRanges(result.path("aggregations").path("price_range"));
        priceRange.setFrequency(parsePriceFrequency(result.path("aggregations").path("prices")));
        return priceRange;
    }


    public static JsonNode aggTags() {
        ObjectNode tag = objectMapper.createObjectNode();
        tag.putObject("terms")
                .put("field", "tag.implicits")
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
                .put("field", "price.middle");
        return priceRange;
    }

    private static JsonNode aggPriceRange() {
        ObjectNode priceRange = objectMapper.createObjectNode();
        priceRange.putObject("percentiles")
                .put("field", "price.middle")
                .putArray("percents")
                .add(0.0).add(40.0).add(70.0).add(100.0);
        return priceRange;
    }

    public static Map<String, Integer> parseTag(JsonNode terms) {
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

    private static FilterPriceRange parsePriceRanges(JsonNode node) {
        FilterPriceRange priceRange = new FilterPriceRange();

        JsonNode values = node.path("values");
        double f0 = values.path("0.0").asDouble();
        double f40 = values.path("40.0").asDouble();
        double f70 = values.path("70.0").asDouble();
        double f100 = values.path("100.0").asDouble();

        if (Double.isNaN(f0)) {
            FilterPriceRange.Segment segment = new FilterPriceRange.Segment(0, 0);
            priceRange.setAll(segment);
            priceRange.setCheap(segment);
            priceRange.setAverage(segment);
            priceRange.setExpensive(segment);
            return priceRange;
        }

        priceRange.setAll(new FilterPriceRange.Segment(f0, f100));
        priceRange.setCheap(new FilterPriceRange.Segment(f0, f40));
        priceRange.setAverage(new FilterPriceRange.Segment(f40, f70));
        priceRange.setExpensive(new FilterPriceRange.Segment(f70, f100));
        return priceRange;
    }
}
