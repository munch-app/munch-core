package munch.api.services.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.services.search.cards.CardParser;
import munch.api.services.search.cards.SearchCard;
import munch.data.clients.PlaceClient;
import munch.data.elastic.query.BoolQuery;
import munch.data.structure.Place;
import munch.data.structure.SearchQuery;
import munch.restful.core.JsonUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 18/10/2017
 * Time: 1:39 AM
 * Project: munch-core
 */
@Singleton
public final class SearchManager {
    private final PlaceClient placeClient;
    private final CardParser cardParser;

    private final InjectedCardManager injectedCardManager;

    @Inject
    public SearchManager(PlaceClient placeClient, CardParser cardParser, InjectedCardManager injectedCardManager) {
        this.placeClient = placeClient;
        this.cardParser = cardParser;
        this.injectedCardManager = injectedCardManager;
    }

    /**
     * @param query query to search
     * @return List of SearchCard
     */
    public List<SearchCard> search(SearchQuery query) {
        query.setRadius(resolveRadius(query));
        List<Place> places = placeClient.getSearchClient().search(query);
        List<SearchCard> cards = cardParser.parseCards(places);
        injectedCardManager.inject(cards, query);
        return cards;
    }

    /**
     * @param query query to count
     * @return total possible place result count
     */
    public Long count(SearchQuery query) {
        query.setRadius(resolveRadius(query));
        return placeClient.getSearchClient().count(query);
    }

    public JsonNode priceAggregation(SearchQuery query) {
        query.setRadius(resolveRadius(query));
        ObjectNode rootNode = JsonUtils.objectMapper.createObjectNode();
        rootNode.put("size", 0);

        ObjectNode priceRange = rootNode.putObject("aggs").putObject("price_range");

        // Setup filters
        ArrayNode must = priceRange.putObject("filter")
                .putObject("bool")
                .putArray("must");
        must.add(BoolQuery.filterTerm("dataType", "Place"));
        BoolQuery.filterLocation(query).ifPresent(must::add);

        // Setup aggregators
        ObjectNode aggs = priceRange.putObject("aggs");
        aggs.putObject("avg_price")
                .putObject("avg")
                .put("field", "price.middle");

        aggs.putObject("price_percents")
                .putObject("percentiles")
                .put("field", "price.middle")
                .putArray("percents")
                .add(0.0).add(30.0).add(70.0).add(100.0);


        JsonNode aggregationResult = placeClient.getSearchClient().search(rootNode)
                .path("aggregations")
                .path("price_range");

        JsonNode percentValues = aggregationResult.path("price_percents")
                .path("values");

        ObjectNode result = JsonUtils.objectMapper.createObjectNode();
        result.put("avg", aggregationResult.path("avg_price").path("value").asDouble());
        result.put("min", percentValues.path("0.0").asDouble());
        result.put("max", percentValues.path("100.0").asDouble());

        result.putObject("cheapRange")
                .put("min", percentValues.path("0.0").asDouble())
                .put("max", percentValues.path("30.0").asDouble());

        result.putObject("averageRange")
                .put("min", percentValues.path("30.0").asDouble())
                .put("max", percentValues.path("70.0").asDouble());

        result.putObject("expensiveRange")
                .put("min", percentValues.path("70.0").asDouble())
                .put("max", percentValues.path("100.0").asDouble());
        return result;
    }

    private static double resolveRadius(SearchQuery query) {
        Double radius = query.getRadius();
        if (radius == null) return 800; // Default radius
        if (radius > 3000) return 3000; // Max radius for nearby search
        return radius;
    }
}
