package munch.articles;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Singleton;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 2:22 AM
 * Project: munch-core
 */
@Singleton
public class ArticleService implements JsonService {

    @Override
    public void route() {
        PATH("/places/:placeId/articles", () -> {
            GET("/list", this::list);

            GET("/:articleId", this::get);
            PUT("/:articleId", this::put);
            DELETE("/:articleId", this::delete);
        });
    }

    private JsonNode list(JsonCall call) {
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        return null;
    }

    private JsonNode get(JsonCall call) {
        String id = call.pathString("articleId");
        return null;
    }

    private JsonNode put(JsonCall call) {
        String id = call.pathString("articleId");
        return null;
    }

    private JsonNode delete(JsonCall call) {
        String id = call.pathString("articleId");
        return null;
    }
}
