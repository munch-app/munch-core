package munch.api.search;

import munch.api.ApiService;
import munch.api.search.named.NamedDelegator;
import munch.data.named.NamedQuery;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.user.client.UserSearchQueryClient;
import munch.user.data.UserSearchQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
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
    private final SearchDelegator searchDelegator;
    private final NamedDelegator namedDelegator;

    private final UserSearchQueryClient userSearchQueryClient;

    @Inject
    public SearchService(SearchRequest.Factory searchRequestFactory, SearchDelegator searchDelegator, NamedDelegator namedDelegator, UserSearchQueryClient userSearchQueryClient) {
        this.searchRequestFactory = searchRequestFactory;
        this.searchDelegator = searchDelegator;
        this.namedDelegator = namedDelegator;
        this.userSearchQueryClient = userSearchQueryClient;
    }

    @Override
    public void route() {
        PATH("/search", () -> {
            // Named is SEO generated query, slug is required to query it
            GET("/named/:slug", this::named);

            // QID is user executed query, executed query contains a unique id
            GET("/qid/:qid", this::qid);

            // Generic user executed search, qid will be generated and returned
            POST("", this::search);
        });
    }

    /**
     * @param call with {qid} in query-string
     * @return SearchQuery
     */
    @Nullable
    public Object qid(JsonCall call) {
        String qid = call.pathString("qid");
        UserSearchQuery userSearchQuery = userSearchQueryClient.get(qid);
        if (userSearchQuery == null) return null;
        return userSearchQuery.getSearchQuery();
    }

    /**
     * @param call with {slug} in path
     * @return NameSearchQuery
     */
    @Nullable
    public NamedQuery named(JsonCall call) {
        String slug = call.pathString("slug");
        return namedDelegator.delegate(slug);
    }

    /**
     * @param call json call with search query
     * @return list of Place
     * @see SearchQuery
     */
    private JsonResult search(JsonCall call) {
        SearchRequest searchRequest = searchRequestFactory.create(call);
        UserSearchQuery userSearchQuery = userSearchQueryClient.create(searchRequest.getUserId(), searchRequest.getSearchQuery());

        // Persist SearchQuery if first Page Search.
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
        result.put("data", searchDelegator.delegate(searchRequest));
        result.put("qid", userSearchQuery.getQid());
        return result;
    }
}
