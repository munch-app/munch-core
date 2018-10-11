package munch.api.search;

import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.search.cards.SearchCard;
import munch.api.search.cards.SearchCardParser;
import munch.api.search.elastic.ElasticQueryUtils;
import munch.api.search.elastic.ElasticSortUtils;
import munch.api.search.inject.SearchCardInjector;
import munch.data.client.ElasticClient;
import munch.data.place.Place;
import munch.restful.core.JsonUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 17/9/18
 * Time: 12:21 PM
 * Project: munch-core
 */
@Singleton
public final class SearchDelegator {
    private static final int PAGE_SIZE = 30;

    private final ElasticClient elasticClient;

    private final SearchCardParser cardParser;
    private final SearchPlaceSorter placeSorter;
    private final SearchCardInjector searchCardInjector;

    @Inject
    public SearchDelegator(ElasticClient elasticClient, SearchCardParser cardParser, SearchPlaceSorter placeSorter, SearchCardInjector searchCardInjector) {
        this.elasticClient = elasticClient;
        this.cardParser = cardParser;
        this.placeSorter = placeSorter;
        this.searchCardInjector = searchCardInjector;
    }

    public List<SearchCard> delegate(SearchRequest request) {
        // Only allow 10 page of results, to prevent abuse
        if (request.getPage() >= 10) return List.of();

        List<Place> places = searchPlaces(request);
        places = placeSorter.sort(places, request);

        List<SearchCard> cards = cardParser.parseCards(places, request);
        searchCardInjector.inject(cards, request);
        return cards;
    }

    private List<Place> searchPlaces(SearchRequest request) {
        if (!isSearchable(request)) return List.of();

        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", request.getPage() * PAGE_SIZE);
        root.put("size", PAGE_SIZE);
        root.set("query", ElasticQueryUtils.make(request));
        root.set("sort", ElasticSortUtils.make(request));
        return elasticClient.searchHitsHits(root);
    }

    private static boolean isSearchable(SearchRequest request) {
        if (request.isBetween()) return false;
        return true;
    }
}
