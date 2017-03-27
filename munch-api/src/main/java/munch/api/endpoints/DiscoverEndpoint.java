package munch.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Singleton;
import munch.api.PlaceRandom;
import munch.document.DocumentQuery;
import munch.restful.server.JsonCall;
import munch.search.SearchQuery;
import munch.struct.Place;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 3:18 AM
 * Project: munch-core
 */
@Singleton
public class DiscoverEndpoint extends MunchEndpoint {

    private final SearchQuery search;
    private final DocumentQuery document;

    private final PlaceRandom placeRandom;

    @Inject
    public DiscoverEndpoint(SearchQuery search, DocumentQuery document, PlaceRandom placeRandom) {
        this.search = search;
        this.document = document;
        this.placeRandom = placeRandom;
    }

    @Override
    public void route() {
        path("/discover", () -> {
            post("", this::discover);
        });
    }

    /**
     * @param call json call
     * @param node json node
     * @return JsonNode discover data
     */
    private JsonNode discover(JsonCall call, JsonNode node) throws IOException {
        Spatial spatial = getSpatial(node);

        JsonNode geoFilter = search.createGeoFilter(spatial.getLat(), spatial.getLng(), 1000);
        ObjectNode bool = newNode();
        bool.set("must", newNode().set("match_all", newNode()));
        bool.set("filter", geoFilter);
        ObjectNode root = newNode();
        root.set("query", newNode().set("bool", bool));
        root.put("size", 20);
        root.set("_source", newArrayNode().add("name"));

        List<Place> places = document.get(search.query(root)
                .stream().map(Place::getId).collect(Collectors.toList()));
        places.forEach(placeRandom::random);
        return nodes(200, places);
    }
}
