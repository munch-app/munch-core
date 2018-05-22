package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import io.searchbox.core.Search;
import munch.api.services.search.*;
import munch.api.services.search.cards.SearchCard;
import munch.data.assumption.AssumptionEngine;
import munch.data.assumption.AssumptionQuery;
import munch.data.clients.PlaceClient;
import munch.data.clients.SearchClient;
import munch.data.elastic.ElasticClient;
import munch.data.elastic.ElasticIndex;
import munch.data.structure.Place;
import munch.data.structure.SearchQuery;
import munch.data.structure.SearchResult;
import munch.restful.core.JsonUtils;
import munch.restful.core.exception.ParamException;
import munch.restful.server.JsonCall;
import munch.restful.server.jwt.TokenAuthenticator;
import munch.user.client.UserSettingClient;
import munch.user.data.UserSetting;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by: Fuxing
 * Date: 15/3/18
 * Time: 10:20 PM
 * Project: munch-core
 */
@Singleton
public final class SearchService extends AbstractService {

    private final TokenAuthenticator<?> authenticator;

    private final SearchClient searchClient;
    private final SearchManager searchManager;

    private final AssumptionEngine assumptionEngine;
    private final PlaceClient.SearchClient placeSearchClient;
    private final UserSettingClient settingClient;

    private final Filter filter;
    private final Location location;

    @Inject
    public SearchService(TokenAuthenticator authenticator, SearchManager searchManager, SearchClient searchClient, AssumptionEngine assumptionEngine, PlaceClient.SearchClient placeSearchClient, Filter filter, Location location, UserSettingClient settingClient) {
        this.authenticator = authenticator;
        this.searchManager = searchManager;
        this.searchClient = searchClient;
        this.assumptionEngine = assumptionEngine;
        this.placeSearchClient = placeSearchClient;

        this.filter = filter;
        this.location = location;
        this.settingClient = settingClient;
    }

    @Override
    public void route() {
        PATH("/search", () -> {
            POST("", this::search);
            POST("/search", this::searchSearch);

            POST("/filter/count", filter::count);
            POST("/filter/price", filter::priceRange);

            GET("/filter/locations/list", location::list);
            GET("/filter/locations/search", location::search);
        });
    }

    /**
     * @param call json call with search query
     * @return list of Place
     * @see SearchQuery
     */
    private List<SearchCard> search(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        authenticator.optionalSubject(call);
        UserSetting setting = authenticator.optionalSubject(call)
                .map(settingClient::get)
                .orElse(null);
        return searchManager.search(query, setting);
    }

    /**
     * @param call json call with search query
     * @return list of Place
     * @see SearchQuery
     */
    private Map<String, JsonNode> searchSearch(JsonCall call) {
        JsonNode request = call.bodyAsJson();
        final String text = ParamException.requireNonNull("text", request.get("text").asText());
        final String latLng = request.path("latLng").asText(null);
        final SearchQuery prevQuery = JsonUtils.toObject(request.path("query"), SearchQuery.class);

        List<AssumptionQueryResult> assumptions = new ArrayList<>();
        for (AssumptionQuery query : assumptionEngine.assume(prevQuery, text)) {
            List<Place> places = placeSearchClient.search(query.getSearchQuery());
            if (!places.isEmpty()) {
                AssumptionQueryResult result = new AssumptionQueryResult();
                result.setSearchQuery(query.getSearchQuery());
                result.setPlaces(places);
                result.setTokens(query.getTokens());
                result.setCount(placeSearchClient.count(query.getSearchQuery()));
                assumptions.add(result);
                break;
            }
        }

        Search search = ElasticClient.createSearch(List.of("Place"), List.of("name^2", "allNames"), text, latLng, 0, 40);
        List<Place> places = searchClient.search(search);
        List<String> suggests = searchClient.suggestText(text, 6);

        return Map.of(
                "suggests", JsonUtils.toTree(suggests),
                "assumptions", JsonUtils.toTree(assumptions),
                "places", JsonUtils.toTree(places)
        );
    }

    public static class Filter {
        private final FilterManager filterManager;

        @Inject
        public Filter(FilterManager filterManager) {
            this.filterManager = filterManager;
        }

        private FilterCount count(JsonCall call) {
            SearchQuery query = call.bodyAsObject(SearchQuery.class);
            return filterManager.filterCount(query);
        }

        private FilterPriceRange priceRange(JsonCall call) {
            SearchQuery query = call.bodyAsObject(SearchQuery.class);
            return filterManager.filterPriceRange(query);
        }
    }

    private static class Location {
        private final SearchClient searchClient;
        private final Supplier<List<SearchResult>> locationSupplier;

        @Inject
        public Location(SearchClient searchClient, ElasticIndex elasticIndex) {
            this.searchClient = searchClient;

            // Every 12 hours, refresh the cache
            this.locationSupplier = Suppliers.memoizeWithExpiration(() -> {
                List<SearchResult> results = new ArrayList<>();
                Iterator<SearchResult> locations = elasticIndex.scroll("Location", "1m");
                locations.forEachRemaining(results::add);

                Iterator<SearchResult> containers = elasticIndex.scroll("Container", "1m");
                containers.forEachRemaining(results::add);
                return results;
            }, 12, TimeUnit.HOURS);

            // Preload
            this.locationSupplier.get();
        }

        private List<SearchResult> list(JsonCall call) {
            return locationSupplier.get();
        }

        private List<SearchResult> search(JsonCall call) {
            final String text = ParamException.requireNonNull("text", call.queryString("text"));

            return searchClient.search(List.of("Location", "Container"), text, null, 0, 20);
        }
    }
}
