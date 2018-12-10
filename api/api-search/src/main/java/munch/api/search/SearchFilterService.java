package munch.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import munch.api.ApiService;
import munch.api.search.filter.*;
import munch.restful.server.JsonCall;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 15/6/18
 * Time: 10:50 PM
 * Project: munch-core
 */
@Singleton
public final class SearchFilterService extends ApiService {

    private final SearchRequest.Factory searchRequestFactory;
    private final FilterResultDelegator resultDelegator;

    private final FilterAreaDatabase areaDatabase;
    private final FilterBetweenDatabase betweenDatabase;

    @Inject
    public SearchFilterService(SearchRequest.Factory searchRequestFactory, FilterResultDelegator resultDelegator, FilterAreaDatabase areaDatabase, FilterBetweenDatabase betweenDatabase) {
        this.searchRequestFactory = searchRequestFactory;
        this.resultDelegator = resultDelegator;
        this.areaDatabase = areaDatabase;
        this.betweenDatabase = betweenDatabase;
    }

    @Override
    public void route() {
        PATH("/search/filter", () -> {
            POST("", this::post);
            GET("/areas", this::getAreas);
            POST("/areas/search", this::searchAreas);
            POST("/between/search", this::betweenSearch);
        });
    }

    /**
     * @param call JsonCall with search query
     * @return FilterResult with interactive SearchQuery building
     */
    public FilterResult post(JsonCall call) {
        SearchRequest request = searchRequestFactory.create(call);
        return resultDelegator.delegate(request);
    }

    /**
     * @return List of Compressed FilterArea
     */
    public List<FilterArea> getAreas(JsonCall call) {
        return areaDatabase.get();
    }

    public List<FilterArea> searchAreas(JsonCall call) {
        JsonNode json = call.bodyAsJson();
        String text = json.path("text").asText();
        return areaDatabase.search(text, 20);
    }

    public List<SearchQuery.Filter.Location.Point> betweenSearch(JsonCall call) {
        return betweenDatabase.search(call);
    }
}
