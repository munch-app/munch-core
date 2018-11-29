package munch.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import munch.api.ApiService;
import munch.api.search.suggest.SuggestDelegator;
import munch.restful.core.JsonUtils;
import munch.restful.core.exception.ParamException;
import munch.restful.server.JsonCall;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 29/11/18
 * Time: 8:15 AM
 * Project: munch-core
 */
@Singleton
public final class SuggestService extends ApiService {

    private final SearchRequest.Factory searchRequestFactory;
    private final SuggestDelegator suggestDelegator;

    @Inject
    public SuggestService(SearchRequest.Factory searchRequestFactory, SuggestDelegator suggestDelegator) {
        this.searchRequestFactory = searchRequestFactory;
        this.suggestDelegator = suggestDelegator;
    }

    @Override
    public void route() {
        PATH("/suggest", () -> {
            POST("", this::suggest);
        });
    }

    /**
     * @param call with search query and text in body
     * @return {assumptions: [], places:[], suggests: []}
     */
    private Map<String, Object> suggest(JsonCall call) {
        JsonNode body = call.bodyAsJson();

        String text = body.path("text").asText();
        SearchQuery query = JsonUtils.toObject(body.path("searchQuery"), SearchQuery.class);

        ParamException.requireNonNull("text", text);
        ParamException.requireNonNull("searchQuery", query);

        SearchRequest searchRequest = searchRequestFactory.create(call, query);
        return suggestDelegator.delegate(text, searchRequest);
    }
}
