package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.StaticJsonClient;
import munch.data.clients.SearchClient;
import munch.data.structure.SearchResult;
import munch.restful.server.JsonCall;

import java.io.IOException;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 13/4/2017
 * Time: 1:48 AM
 * Project: munch-core
 */
@Singleton
public class LocationService extends AbstractService {

    private final SearchClient searchClient;
    private final StaticJsonClient jsonResource;

    @Inject
    public LocationService(SearchClient searchClient, StaticJsonClient jsonResource) {
        this.searchClient = searchClient;
        this.jsonResource = jsonResource;
    }

    /**
     * Endpoint: /v/neighborhood/*
     */
    @Override
    public void route() {
        PATH("/locations", () -> {
            GET("/popular", this::popular);
            HEAD("/popular", this::popularHead);

            // Suggest Location when user is searching with location service
            GET("/suggest", this::suggest);
            GET("/search", this::search);
        });
    }

    private JsonNode popular(JsonCall call) throws IOException {
        call.response().header("Last-Modified", "1505473814000");
        return jsonResource.getResource("locations-popular.json");
    }

    private JsonNode popularHead(JsonCall call) {
        call.response().header("Last-Modified", "1505473814000");
        return nodes(200);
    }

    /**
     * ?text={String}&size={Int}
     *
     * @param call json call
     * @return List of Location or empty
     * code: 200 = ok
     */
    private List<SearchResult> suggest(JsonCall call) {
        String text = call.queryString("text");
        int size = call.queryInt("size", 10);
        return searchClient.suggest(List.of("Location", "Container"), text, null, size);
    }

    /**
     * ?text={String}&size={Int}
     *
     * @param call json call
     * @return List of Location or empty
     * code: 200 = ok
     */
    private List<SearchResult> search(JsonCall call) {
        String text = call.queryString("text");
        int size = call.queryInt("size", 10);
        return searchClient.search(List.of("Location", "Container"), text, size);
    }
}
