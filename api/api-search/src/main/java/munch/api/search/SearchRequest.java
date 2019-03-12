package munch.api.search;

import munch.api.ApiRequest;
import munch.api.search.filter.FilterAreaDatabase;
import munch.data.elastic.ElasticUtils;
import munch.data.location.Area;
import munch.restful.core.JsonUtils;
import munch.restful.server.JsonCall;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is the more generic SearchRequest loader
 * Used for: suggest, search, filter
 * <p>
 * Runner.Request is used for search with cards tools
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
     * @return Current Feature
     */
    public SearchQuery.Feature getFeature() {
        return searchQuery.getFeature();
    }

    /**
     * @param features to check if any chosen
     * @return whether any match
     */
    public boolean isFeature(SearchQuery.Feature... features) {
        SearchQuery.Feature current = getFeature();
        for (SearchQuery.Feature feature : features) {
            if (current == feature) return true;
        }
        return false;
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

    /**
     * @return if is anywhere, will resolve to anywhere if given type is invalid
     */
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

    @NotNull
    public List<SearchQuery.Filter.Location.Point> getPoints() {
        if (searchQuery.getFilter().getLocation().getPoints() == null) return List.of();
        return searchQuery.getFilter().getLocation().getPoints();
    }

    public String getPointsCentroid() {
        double[] centroid = ElasticUtils.Spatial.getCentroid(getPoints(), SearchQuery.Filter.Location.Point::getLatLng);
        return centroid[0] + "," + centroid[1];
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

        private final FilterAreaDatabase areaDatabase;

        @Inject
        public Factory(FilterAreaDatabase areaDatabase) {
            this.areaDatabase = areaDatabase;
        }

        /**
         * @param call JsonCall with body as SearchQuery
         * @return SearchRequest
         */
        public SearchRequest create(JsonCall call) {
            return create(call, call.bodyAsObject(SearchQuery.class));
        }

        /**
         * @param call        JsonCall
         * @param searchQuery custom search query
         * @return SearchRequest
         */
        public SearchRequest create(JsonCall call, SearchQuery searchQuery) {
            SearchQuery.validate(searchQuery);
            resolve(searchQuery);
            return new SearchRequest(call, searchQuery);
        }

        /**
         * @param searchQuery needs to be resolved as some data are truncated
         */
        private void resolve(SearchQuery searchQuery) {
            List<Area> areas = searchQuery.getFilter().getLocation().getAreas();
            if (!areas.isEmpty()) {
                List<Area> resolved = areas.stream()
                        .map(areaDatabase::resolve)
                        .collect(Collectors.toList());
                searchQuery.getFilter().getLocation().setAreas(resolved);
            }
        }
    }
}
