package munch.api.endpoints;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.PlaceRandom;
import munch.api.endpoints.service.DiscoverService;
import munch.document.DocumentQuery;
import munch.restful.server.JsonCall;
import munch.struct.Article;
import munch.struct.Graphic;
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

    private final DiscoverService discover;

    private final DocumentQuery document;
    private final PlaceRandom placeRandom;

    @Inject
    public PlaceEndpoint(DiscoverService discover, DocumentQuery document, PlaceRandom placeRandom) {
        this.discover = discover;
        this.document = document;
        this.placeRandom = placeRandom;
    }

    @Override
    public void route() {
        path("/places", () -> {
            post("/discover", discover::discover);

            // TODO gallery/articles pagination
            get("/:placeId/gallery", this::gallery);
            get("/:placeId/articles", this::articles);
        });
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
