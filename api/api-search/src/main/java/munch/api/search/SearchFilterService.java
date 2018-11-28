package munch.api.search;

import munch.api.ApiService;
import munch.api.search.filter.FilterArea;
import munch.api.search.filter.FilterAreaDatabase;
import munch.api.search.filter.FilterResult;
import munch.api.search.filter.FilterResultDelegator;
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

    @Inject
    public SearchFilterService(SearchRequest.Factory searchRequestFactory, FilterResultDelegator resultDelegator, FilterAreaDatabase areaDatabase) {
        this.searchRequestFactory = searchRequestFactory;
        this.resultDelegator = resultDelegator;
        this.areaDatabase = areaDatabase;
    }

    @Override
    public void route() {
        PATH("/search/filter", () -> {
            POST("", this::post);
            GET("/areas", this::getAreas);
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
}
