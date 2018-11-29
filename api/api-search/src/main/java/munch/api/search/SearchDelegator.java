package munch.api.search;

import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.search.cards.SearchCard;
import munch.api.search.cards.SearchCardParser;
import munch.api.search.elastic.ElasticQueryUtils;
import munch.api.search.elastic.ElasticSortUtils;
import munch.api.search.plugin.SearchCardPlugin;
import munch.data.client.ElasticClient;
import munch.data.place.Place;
import munch.restful.core.JsonUtils;
import munch.taste.GlobalTasteClient;

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
    private final ElasticClient elasticClient;

    private final SearchCardParser cardParser;
    private final SearchCardPlugin.Runner cardInjector;

    private final GlobalTasteClient tasteClient;

    @Inject
    public SearchDelegator(ElasticClient elasticClient, SearchCardParser cardParser, SearchCardPlugin.Runner cardInjector, GlobalTasteClient tasteClient) {
        this.elasticClient = elasticClient;
        this.cardParser = cardParser;
        this.cardInjector = cardInjector;
        this.tasteClient = tasteClient;
    }

    /**
     * @param request to execute
     * @return results in SearchCard
     */
    public List<SearchCard> delegate(SearchRequest request) {
        if (request.getPage() >= getMaxPage(request)) return List.of();

        List<Place> places = searchPlaces(request);
        places = tasteClient.sort(places, request.getLocalTime());

        List<SearchCard> cards = cardParser.parse(places, request);
        cardInjector.run(cards, request);
        return cards;
    }

    /**
     * @param request search request
     * @return List of Place if empty if request don't support search
     */
    private List<Place> searchPlaces(SearchRequest request) {
        // Only Search Screen returns Place results
        if (request.getScreen() != SearchRequest.Screen.search) return List.of();

        int pageSize = getPageSize(request);
        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", request.getPage() * pageSize);
        root.put("size", pageSize);
        root.set("query", ElasticQueryUtils.make(request));
        root.set("sort", ElasticSortUtils.make(request));
        return elasticClient.searchHitsHits(root);
    }

    /**
     * For Location Type: Where, Anywhere, Nearby:
     * Only allow 10 page of results, to prevent abuse
     * <p>
     * For Location Type: Between
     * Only 1 page of result due to a targeted search
     * <p>
     * Page starts from 0
     *
     * @param request to find max number page
     * @return max pages
     */
    private static int getMaxPage(SearchRequest request) {
        if (request.isBetween()) return 1;
        return 10;
    }

    /**
     * LocationType.Between: 100
     * Else: 30
     *
     * @param request to find page size
     * @return size per page
     */
    private static int getPageSize(SearchRequest request) {
        if (request.isBetween()) return 100;
        return 30;
    }
}
