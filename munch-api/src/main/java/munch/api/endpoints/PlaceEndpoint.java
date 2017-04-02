package munch.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.PlaceRandom;
import munch.document.DocumentQuery;
import munch.restful.server.JsonCall;

/**
 * Created by: Fuxing
 * Date: 2/4/2017
 * Time: 4:15 PM
 * Project: munch-core
 */
@Singleton
public class PlaceEndpoint extends MunchEndpoint {

    private final DocumentQuery document;

    private final PlaceRandom placeRandom;

    @Inject
    public PlaceEndpoint(DocumentQuery document, PlaceRandom placeRandom) {
        this.document = document;
        this.placeRandom = placeRandom;
    }

    @Override
    public void route() {
        path("/places", () -> {
            // TODO gallery/articles pagination
            get("/:placeId/gallery", this::gallery);
            get("/:placeId/articles", this::articles);
        });
    }

    private JsonNode gallery(JsonCall call) {
        String placeId = call.pathString("placeId");
        return null;
    }

    private JsonNode articles(JsonCall call) {
        String placeId = call.pathString("placeId");
        return null;
    }
}
