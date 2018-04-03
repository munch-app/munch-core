package munch.api.services;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import munch.api.services.discover.FilterCount;
import munch.api.services.discover.FilterManager;
import munch.api.services.discover.FilterPriceRange;
import munch.api.services.discover.SearchManager;
import munch.api.services.discover.cards.SearchCard;
import munch.data.clients.SearchClient;
import munch.data.elastic.ElasticIndex;
import munch.data.structure.SearchQuery;
import munch.data.structure.SearchResult;
import munch.restful.core.exception.ParamException;
import munch.restful.server.JsonCall;
import munch.restful.server.jwt.AuthenticatedToken;
import munch.restful.server.jwt.TokenAuthenticator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by: Fuxing
 * Date: 15/3/18
 * Time: 10:20 PM
 * Project: munch-core
 */
@Singleton
public final class DiscoverService extends AbstractService {

    private final Supplier<List<SearchResult>> locationSupplier;

    private final TokenAuthenticator authenticator;

    private final SearchClient searchClient;
    private final SearchManager searchManager;
    private final FilterManager filterManager;

    @Inject
    public DiscoverService(TokenAuthenticator authenticator, SearchManager searchManager, SearchClient searchClient, ElasticIndex elasticIndex, FilterManager filterManager) {
        this.authenticator = authenticator;
        this.searchManager = searchManager;
        this.searchClient = searchClient;
        this.filterManager = filterManager;

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

    @Override
    public void route() {
        PATH("/discover", () -> {
            POST("", this::discover);
            POST("/filter/count", this::filterCount);
            POST("/filter/price/range", this::filterPriceRange);

            Location location = new Location();
            GET("/filter/locations/list", location::list);
            GET("/filter/locations/search", location::search);
        });
    }

    /**
     * @param call json call with search query
     * @return list of Place
     * @see SearchQuery
     */
    private List<SearchCard> discover(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        String userId = authenticator.optional(call).map(AuthenticatedToken::getSubject).orElse(null);
        return searchManager.search(query, userId);
    }

    private FilterCount filterCount(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        return filterManager.filterCount(query);
    }

    private FilterPriceRange filterPriceRange(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        return filterManager.filterPriceRange(query);
    }

    private class Location {
        private List<SearchResult> list(JsonCall call) {
            return locationSupplier.get();
        }

        private List<SearchResult> search(JsonCall call) {
            final String text = ParamException.requireNonNull("text", call.queryString("text"));
            final String latLng = call.queryString("latLng");

            return searchClient.search(List.of("Location", "Container"), text, latLng, 0, 20);
        }
    }
}
