package munch.api.services.discover;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.data.clients.PlaceClient;
import munch.data.elastic.ElasticClient;
import munch.data.elastic.query.BoolQuery;
import munch.data.structure.SearchQuery;
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

    public FilterData filter(SearchQuery query) {
        query.setRadius(SearchManager.resolveRadius(query));

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("size", 0);
        rootNode.putObject("query").set("bool", boolQuery.make(query));
        ObjectNode aggs = rootNode.putObject("aggs");
        aggs.set("prices", aggPrices());
        aggs.set("tags", aggTags());

        JsonNode result = elasticClient.postSearch(rootNode);

        FilterData filterData = new FilterData();
        filterData.setCount(placeClient.getSearchClient().count(query));
        filterData.setPriceRange(parsePrices(result.path("aggregations").path("prices")));
        filterData.setTag(parseTag(result.path("aggregations").path("prices")));
        return filterData;
    }

    private static JsonNode aggTags() {
        ObjectNode tag = objectMapper.createObjectNode();
        tag.putObject("terms")
                .put("field", "tag.explicits");
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
                .put("interval", 1)
                .put("field", "price.middle");
        return priceRange;
    }

    private static FilterData.PriceRange parsePrices(JsonNode prices) {
        Map<Double, Integer> frequency = new HashMap<>();
        int sum = 0;

        for (JsonNode object : prices.path("buckets")) {
            double key = object.path("key").asDouble();
            int count = object.path("doc_count").asInt();
            frequency.put(key, count);
            sum += count;
        }

        FilterData.PriceRange priceRange = new FilterData.PriceRange();
        priceRange.setFrequency(frequency);
        // TODO
        // 1 - 40
        // 40 - 70
        // 70 - 100
        return priceRange;
    }

    private static FilterData.Tag parseTag(JsonNode terms) {
        Map<String, Integer> counts = new HashMap<>();

        for (JsonNode object : terms.path("buckets")) {
            String key = object.path("key").asText();
            int count = object.path("doc_count").asInt();
            counts.put(key, count);
        }

        FilterData.Tag tag = new FilterData.Tag();
        tag.setCounts(counts);
        return tag;
    }
}
