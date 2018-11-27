package munch.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import munch.api.ApiService;
import munch.api.search.data.NamedSearchQuery;
import munch.api.search.data.SearchQuery;
import munch.api.search.named.NamedDelegator;
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
    private final NamedDelegator namedDelegator;

    private final UserSearchQueryClient userSearchQueryClient;

    @Inject
    public SearchService(SearchRequest.Factory searchRequestFactory, SearchDelegator searchRequestDelegator, SuggestDelegator suggestDelegator, NamedDelegator namedDelegator, UserSearchQueryClient userSearchQueryClient) {
        this.searchRequestFactory = searchRequestFactory;
        this.searchRequestDelegator = searchRequestDelegator;
        this.suggestDelegator = suggestDelegator;
        this.namedDelegator = namedDelegator;
        this.userSearchQueryClient = userSearchQueryClient;
    }

    @Override
    public void route() {
        PATH("/search", () -> {
            GET("/named/:named", this::named);
            GET("/qid/:qid", this::qid);

            POST("", this::search);
            POST("/suggest", this::suggest);
        });
    }

    public NamedSearchQuery named(JsonCall call) {
        String named = call.pathString("named");
        return namedDelegator.delegate(named);
    }

    public Object qid(JsonCall call) {
        String qid = call.pathString("qid");
        UserSearchQuery userSearchQuery = userSearchQueryClient.get(qid);
        if (userSearchQuery == null) return null;
        return userSearchQuery.getSearchQuery();
    }

    /**
     * @param call json call with search query
     * @return list of Place
     * @see SearchQuery
     */
    private JsonResult search(JsonCall call) {
        SearchRequest searchRequest = searchRequestFactory.create(call);
        UserSearchQuery userSearchQuery = userSearchQueryClient.create(searchRequest.getUserId(), searchRequest.getSearchQuery());

        if (searchRequest.getPage() == 0) {
            CompletableFuture.runAsync(() -> {
                try {
                    userSearchQueryClient.put(userSearchQuery);
                } catch (Exception e) {
                    logger.warn("Failed to persist UserSearchQuery: {}", userSearchQuery, e);
                }
            });
        }

        JsonResult result = JsonResult.ok();
        result.put("data", searchRequestDelegator.delegate(searchRequest));
        result.put("qid", userSearchQuery.getQid());
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
