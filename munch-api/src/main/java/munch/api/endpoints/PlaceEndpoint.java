package munch.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.PlaceRandom;
import munch.restful.server.JsonCall;
import munch.struct.Article;
import munch.struct.Graphic;
import munch.struct.Place;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 2/4/2017
 * Time: 4:15 PM
 * Project: munch-core
 */
@Singleton
public class PlaceEndpoint extends AbstractEndpoint {

    private final DocumentQuery document;
    private final PlaceRandom placeRandom;

    @Inject
    public PlaceEndpoint(DocumentQuery document, PlaceRandom placeRandom) {
        this.document = document;
        this.placeRandom = placeRandom;
    }

    @Override
    public void route() {
        PATH("/places", () -> {
            POST("/search", this::search);

            // TODO gallery/articles pagination
            GET("/:placeId/gallery", this::gallery);
            GET("/:placeId/articles", this::articles);
        });
    }

    private List<Place> search(JsonCall call, JsonNode node) {
        // TODO
        if (node.path("location").has("lat")) {
            double lat = node.path("location").path("lat").asDouble();
        }

        String query = node.path("query").asText(null);
        return null;
    }

    private List<Graphic> gallery(JsonCall call) {
        String placeId = call.pathString("placeId");
        return placeRandom.randomGraphics(RandomUtils.nextInt(20, 30));
    }

    private List<Article> articles(JsonCall call) {
        String placeId = call.pathString("placeId");
        return placeRandom.randomArticles(RandomUtils.nextInt(8, 12));
    }
}
