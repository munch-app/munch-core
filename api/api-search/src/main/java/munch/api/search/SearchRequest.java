package munch.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import munch.api.ApiRequest;
import munch.api.search.data.SearchQuery;
import munch.data.location.Area;
import munch.restful.core.JsonUtils;
import munch.restful.server.JsonCall;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * This is the more generic SearchRequest loader
 * Used for: suggest, search, filter
 * <p>
 * SearchCardInjector.Request is used for search with cards tools
 * <p>
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

    private SearchRequest(JsonCall call, SearchQuery searchQuery) {
        this.call = call;
        this.searchQuery = searchQuery;
        this.userId = call.get(ApiRequest.class).optionalUserId().orElse(null);
        this.latLng = call.get(ApiRequest.class).optionalLatLng().orElse(null);
        this.localDateTime = call.get(ApiRequest.class).optionalLocalTime().orElse(null);
    }

    /**
     * @return HTTP JsonCall Request
     */
    public JsonCall getCall() {
        return call;
    }

    /**
     * @return page number user is on
     */
    public int getPage() {
        return call.queryInt("page", 0);
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

    public boolean isLocationAny(SearchQuery.Filter.Location.Type... types) {
        for (SearchQuery.Filter.Location.Type type : types) {
            switch (type) {
                case Where:
                    if (isWhere()) return true;
                    break;
                case Nearby:
                    if (isNearby()) return true;
                    break;
                case Between:
                    if (isBetween()) return true;
                    break;
                case Anywhere:
                    if (isAnywhere()) return true;
                    break;
            }
        }
        return false;
    }

    public boolean isAnywhere() {
        if (searchQuery.getFilter().getLocation().getType() == SearchQuery.Filter.Location.Type.Anywhere) return true;
        if (isNearby()) return false;
        if (isBetween()) return false;
        if (isWhere()) return false;
        return true;
    }

    public boolean isNearby() {
        if (!hasUserLatLng()) return false;
        return searchQuery.getFilter().getLocation().getType() == SearchQuery.Filter.Location.Type.Nearby;
    }

    public boolean isWhere() {
        if (!hasArea()) return false;
        return searchQuery.getFilter().getLocation().getType() == SearchQuery.Filter.Location.Type.Where;
    }

    public boolean isBetween() {
        if (getPoints().size() < 1) return false;
        return searchQuery.getFilter().getLocation().getType() == SearchQuery.Filter.Location.Type.Between;
    }

    public boolean hasArea() {
        return !getAreas().isEmpty();
    }

    public List<Area> getAreas() {
        if (searchQuery.getFilter().getLocation().getAreas() == null) {
            searchQuery.getFilter().getLocation().setAreas(new ArrayList<>());
        }
        return searchQuery.getFilter().getLocation().getAreas();
    }

    public List<SearchQuery.Filter.Location.Point> getPoints() {
        if (searchQuery.getFilter().getLocation().getPoints() == null) return List.of();
        return searchQuery.getFilter().getLocation().getPoints();
    }

    /**
     * @return whether user Location exists
     */
    public boolean hasUserLatLng() {
        return latLng != null;
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
     * @param defaultName if no name is found
     * @return Location name of search
     */
    public String getLocationName(String defaultName) {
        if (isAnywhere()) return "Anywhere";
        if (isBetween()) return "Between";
        if (isNearby()) return "Nearby";
        if (isWhere() && getAreas().size() == 1) {
            String name = getAreas().get(0).getName();
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
        return new SearchRequest(call, searchQuery);
    }

    /**
     * @param query to replace
     * @return cloned search request, with query replaced
     */
    public SearchRequest cloneWith(SearchQuery query) {
        return new SearchRequest(call, query);
    }

    @Singleton
    public static final class Factory {
        public SearchRequest create(JsonCall call) {
            SearchQuery searchQuery = call.bodyAsObject(SearchQuery.class);
            fix(searchQuery);
            return new SearchRequest(call, searchQuery);
        }

        public SearchRequest create(JsonCall call, SearchQuery searchQuery) {
            fix(searchQuery);
            return new SearchRequest(call, searchQuery);
        }

        public SearchRequest create(JsonCall call, Function<JsonNode, SearchQuery> mapper) {
            SearchQuery searchQuery = mapper.apply(call.bodyAsJson());
            fix(searchQuery);
            return new SearchRequest(call, searchQuery);
        }

        private static void fix(SearchQuery query) {
            if (query.getFilter() == null) query.setFilter(new SearchQuery.Filter());
            if (query.getSort() == null) query.setSort(new SearchQuery.Sort());

            if (query.getFilter().getLocation() == null) {
                query.getFilter().setLocation(new SearchQuery.Filter.Location());
            }
        }
    }
}
