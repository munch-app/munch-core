package munch.api.search.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.search.SearchRequest;
import munch.api.search.elastic.ElasticQueryUtils;
import munch.data.client.ElasticClient;
import munch.restful.core.JsonUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 28/11/18
 * Time: 4:31 AM
 * Project: munch-core
 */
@Singleton
public final class FilterResultDelegator {

    private final ElasticClient elasticClient;
    private final FilterTagDatabase tagDatabase;
    private final FilterPriceGrapher priceGrapher;

    @Inject
    public FilterResultDelegator(ElasticClient elasticClient, FilterTagDatabase tagDatabase, FilterPriceGrapher priceGrapher) {
        this.elasticClient = elasticClient;
        this.tagDatabase = tagDatabase;
        this.priceGrapher = priceGrapher;
    }

    public FilterResult delegate(SearchRequest request) {
        long count;
        Map<String, Integer> tags;
        Map<Double, Integer> frequency;

        if (!request.hasPrice()) {
            JsonNode agg = FilterElasticUtils.agg(true, true, true);
            JsonNode aggregations = postAggregation(request, agg);

            count = FilterElasticUtils.parseCount(aggregations);
            tags = FilterElasticUtils.parseTags(aggregations);
            frequency = FilterElasticUtils.parsePrice(aggregations);
        } else {
            JsonNode agg = FilterElasticUtils.agg(false, true, true);
            JsonNode aggregations = postAggregation(request, agg);
            count = FilterElasticUtils.parseCount(aggregations);
            tags = FilterElasticUtils.parseTags(aggregations);

            request.getSearchQuery().getFilter().setPrice(null);
            agg = FilterElasticUtils.agg(true, false, false);
            aggregations = postAggregation(request, agg);

            frequency = FilterElasticUtils.parsePrice(aggregations);
        }

        return parse(count, tags, frequency);
    }

    private FilterResult parse(long count, Map<String, Integer> tags, Map<Double, Integer> frequency) {
        FilterResult result = new FilterResult();
        result.setCount(count);
        result.setTagGraph(tagDatabase.getGraph(tags));
        result.setPriceGraph(priceGrapher.getGraph(frequency));
        return result;
    }

    /**
     * @param request search request
     * @param aggs    node to post
     * @return result as JsonNode
     */
    private JsonNode postAggregation(SearchRequest request, JsonNode aggs) {
        ObjectNode rootNode = JsonUtils.createObjectNode();
        rootNode.put("size", 0);
        rootNode.set("query", ElasticQueryUtils.make(request));
        rootNode.set("aggs", aggs);

        JsonNode result = elasticClient.search(rootNode);
        return result.path("aggregations");
    }
}
