package munch.permutation;

import com.mashape.unirest.http.HttpMethod;
import com.typesafe.config.ConfigFactory;
import munch.api.search.data.FilterResult;
import munch.api.search.data.SearchQuery;
import munch.restful.client.RestfulRequest;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 31/10/18
 * Time: 1:28 AM
 * Project: munch-core
 */
@Singleton
public final class APISearchClient {
    protected final String apiUrl;

    @Inject
    public APISearchClient() {
        this.apiUrl = ConfigFactory.load().getString("api.url");
    }

    /**
     * Even through this method is not efficient but it is the most modularised
     *
     * @param searchQuery with
     * @return List of Card results
     */
    public long count(SearchQuery searchQuery) {
        RestfulRequest request = new RestfulRequest(HttpMethod.POST, apiUrl + "/search/filter");
        request.body(searchQuery);
        FilterResult result = request.asDataObject(FilterResult.class);
        return result.getCount();
    }
}
