package munch.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import munch.api.ApiService;
import munch.api.search.data.SearchQuery;
import munch.api.search.elastic.ElasticQueryUtils;
import munch.restful.core.JsonUtils;
import munch.restful.server.JsonCall;
import munch.restful.server.jwt.TokenAuthenticator;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * Created by: Fuxing
 * Date: 16/6/18
 * Time: 6:33 PM
 * Project: munch-core
 */
public final class SearchRequest {
    private final JsonCall call;
    private final String userId;
    private final SearchQuery searchQuery;

    private final String latLng;
    private final LocalDateTime localDateTime;

    private SearchRequest(JsonCall call, @Nullable String userId, SearchQuery searchQuery) {
        this(call, userId, searchQuery, ApiService.optionalUserLatLng(call).orElse(null));
    }

    private SearchRequest(JsonCall call, String userId, SearchQuery searchQuery, String latLng) {
        this.call = call;
        this.userId = userId;
        this.searchQuery = searchQuery;
        this.latLng = latLng;

        this.localDateTime = ApiService.optionalUserLocalTime(call).orElse(null);
    }

    /**
     * @return HTTP JsonCall Request
     */
    public JsonCall getCall() {
        return call;
    }

    /**
     * @return userId doing the search Request
     */
    @Nullable
    public String getUserId() {
        return userId;
    }

    /**
     * @return Actual SearchQuery itself
     */
    public SearchQuery getSearchQuery() {
        return searchQuery;
    }

    /**
     * @return whether user Location exists
     */
    public boolean hasUserLatLng() {
        return latLng != null;
    }

    /**
     * @return whether user provide an area to search
     */
    public boolean hasArea() {
        if (searchQuery.getFilter() == null) return false;
        return searchQuery.getFilter().getArea() != null;
    }

    /**
     * @return whether there is price data
     */
    public boolean hasPrice() {
        if (searchQuery.getFilter() == null) return false;
        if (searchQuery.getFilter().getPrice() == null) return false;
        if (searchQuery.getFilter().getPrice().getMin() != null) return true;
        if (searchQuery.getFilter().getPrice().getMax() != null) return true;
        return false;
    }

    /**
     * @return whether user set search to anywhere
     */
    public boolean isAnywhere() {
        if (hasUserLatLng()) return false;
        if (hasArea()) return false;
        return true;
    }

    /**
     * @param defaultName if no name is found
     * @return Location name of search
     */
    public String getLocationName(String defaultName) {
        if (searchQuery.getFilter().getArea() != null) {
            String name = searchQuery.getFilter().getArea().getName();
            if (StringUtils.isNotBlank(name)) return name;
        }
        return defaultName;
    }

    /**
     * @return user latitude longitude
     */
    @Nullable
    public String getLatLng() {
        return latLng;
    }

    /**
     * @return user search radius in metres
     */
    public double getRadius() {
        return 800;
    }

    /**
     * @return LocalDateTime of user
     */
    @Nullable
    public LocalDateTime getLocalTime() {
        return localDateTime;
    }

    /**
     * @return Deep Copied SearchRequest
     */
    public SearchRequest deepCopy() {
        SearchQuery searchQuery = JsonUtils.deepCopy(this.searchQuery, SearchQuery.class);
        return new SearchRequest(call, userId, searchQuery, latLng);
    }

    /**
     * @param query to replace
     * @return cloned search request, with query replaced
     */
    public SearchRequest cloneWith(SearchQuery query) {
        return new SearchRequest(call, userId, query, latLng);
    }

    /**
     * @return Elastic Query
     */
    public JsonNode createElasticQuery() {
        return ElasticQueryUtils.make(this);
    }

    @Singleton
    public static final class Factory {
        private final TokenAuthenticator<?> authenticator;

        @Inject
        public Factory(TokenAuthenticator authenticator) {
            this.authenticator = authenticator;
        }

        public SearchRequest create(JsonCall call) {
            String userId = authenticator.optionalSubject(call).orElse(null);
            SearchQuery searchQuery = call.bodyAsObject(SearchQuery.class);
            SearchQuery.fix(searchQuery);
            return new SearchRequest(call, userId, searchQuery);
        }

        public SearchRequest create(JsonCall call, SearchQuery searchQuery) {
            String userId = authenticator.optionalSubject(call).orElse(null);
            SearchQuery.fix(searchQuery);
            return new SearchRequest(call, userId, searchQuery);
        }

        public SearchRequest create(JsonCall call, Function<JsonNode, SearchQuery> mapper) {
            String userId = authenticator.optionalSubject(call).orElse(null);
            SearchQuery searchQuery = mapper.apply(call.bodyAsJson());
            SearchQuery.fix(searchQuery);
            return new SearchRequest(call, userId, searchQuery);
        }
    }

}
