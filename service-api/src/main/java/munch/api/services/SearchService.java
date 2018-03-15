package munch.api.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.services.search.SearchManager;
import munch.api.services.search.cards.SearchCard;
import munch.data.structure.SearchQuery;
import munch.restful.server.JsonCall;
import munch.restful.server.jwt.AuthenticatedToken;
import munch.restful.server.jwt.TokenAuthenticator;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 12/7/2017
 * Time: 12:54 PM
 * Project: munch-core
 */
@Singleton
public class SearchService extends AbstractService {

    private final TokenAuthenticator authenticator;

    private final SearchManager searchManager;

    @Inject
    public SearchService(TokenAuthenticator authenticator, SearchManager searchManager) {
        this.authenticator = authenticator;
        this.searchManager = searchManager;
    }

    @Override
    public void route() {
        PATH("/search", () -> {
            POST("", this::search);
        });
    }

    /**
     * @param call json call with search query
     * @return list of Place
     * @see SearchQuery
     */
    private List<SearchCard> search(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        String userId = authenticator.optional(call).map(AuthenticatedToken::getSubject).orElse(null);

        // TODO Assumption, Location & Tag
        //
        return searchManager.search(query, userId);
    }
}
