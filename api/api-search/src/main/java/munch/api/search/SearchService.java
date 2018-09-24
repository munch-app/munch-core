package munch.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import munch.api.ApiService;
import munch.api.search.data.SearchQuery;
import munch.api.search.suggest.SuggestDelegator;
import munch.restful.core.JsonUtils;
import munch.restful.core.exception.ParamException;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.user.client.UserSearchQueryClient;
import munch.user.data.UserSearchQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Created by: Fuxing
 * Date: 15/3/18
 * Time: 10:20 PM
 * Project: munch-core
 */
@Singleton
public final class SearchService extends ApiService {
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);


    private final SearchRequest.Factory searchRequestFactory;
    private final SearchDelegator searchRequestDelegator;
    private final SuggestDelegator suggestDelegator;

    private final UserSearchQueryClient userSearchQueryClient;

    @Inject
    public SearchService(SearchRequest.Factory searchRequestFactory, SearchDelegator searchRequestDelegator, SuggestDelegator suggestDelegator, UserSearchQueryClient userSearchQueryClient) {
        this.searchRequestFactory = searchRequestFactory;
        this.searchRequestDelegator = searchRequestDelegator;
        this.suggestDelegator = suggestDelegator;
        this.userSearchQueryClient = userSearchQueryClient;
    }

    @Override
    public void route() {
        PATH("/search", () -> {
            POST("", this::search);
            POST("/suggest", this::suggest);
        });
    }

    /**
     * @param call json call with search query
     * @return list of Place
     * @see SearchQuery
     */
    private JsonResult search(JsonCall call) {
        SearchRequest searchRequest = searchRequestFactory.create(call);
        String userId = searchRequest.getUserId();
        UserSearchQuery userSearchQuery = userSearchQueryClient.create(userId, searchRequest.getSearchQuery());

        // Save UserSearchHistory
        CompletableFuture.runAsync(() -> {
            try {
                userSearchQueryClient.put(userSearchQuery);
            } catch (Exception e) {
                logger.warn("Failed to persist UserSearchQuery: {}", userSearchQuery, e);
            }
        });

        JsonResult result = JsonResult.ok(searchRequestDelegator.delegate(searchRequest));
        result.put("queryId", userSearchQuery.getQueryId());
        return result;
    }

    /**
     * @param call json call with search query
     * @return list of Place
     * @see SearchQuery
     */
    private Map<String, Object> suggest(JsonCall call) {
        JsonNode request = call.bodyAsJson();
        String text = ParamException.requireNonNull("text", request.path("text").asText());
        SearchRequest searchRequest = searchRequestFactory.create(call, node ->
                JsonUtils.toObject(node.path("searchQuery"), SearchQuery.class)
        );
        return suggestDelegator.delegate(text, searchRequest);
    }
}
