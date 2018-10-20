package munch.api.feed;

import munch.api.ApiService;
import munch.data.client.PlaceCachedClient;
import munch.feed.*;
import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 11/10/18
 * Time: 2:15 PM
 * Project: munch-core
 */
@Singleton
public final class FeedService extends ApiService {
    private final PlaceCachedClient placeClient;
    private final ImageFeedClient imageFeedClient;
    private final ArticleFeedClient articleFeedClient;

    @Inject
    public FeedService(PlaceCachedClient placeClient, ImageFeedClient imageFeedClient, ArticleFeedClient articleFeedClient) {
        this.placeClient = placeClient;
        this.imageFeedClient = imageFeedClient;
        this.articleFeedClient = articleFeedClient;
    }

    @Override
    public void route() {
        PATH("/feed", () -> {
            GET("/articles", this::articles);
            GET("/images", this::images);
        });
    }

    private JsonResult result(NextNodeList<? extends FeedItem> items) {
        Set<String> placeIds = new HashSet<>();
        items.forEach(feedItem -> {
            feedItem.getPlaces().forEach(place -> {
                placeIds.add(place.getPlaceId());
            });
        });

        JsonResult result = JsonResult.ok();
        result.put("data", items);
        result.put("places", placeClient.get(placeIds));

        if (items.hasNext()) {
            String sort = items.getNextString("sort", null);
            if (sort != null) {
                result.put("next", Map.of("sort", sort));
            }
        }
        return result;
    }

    public JsonResult images(JsonCall call) {
        String country = call.queryString("country", "sgp");
        String latLng = call.queryString("latLng", "1.3521,103.8198");
        String nextSort = call.queryString("next.sort", null);
        int size = call.querySize(20, 30);

        NextNodeList<ImageFeedItem> items = imageFeedClient.get(country, latLng, nextSort, size);
        return result(items);
    }

    public JsonResult articles(JsonCall call) {
        String country = call.queryString("country", "sgp");
        String latLng = call.queryString("latLng", "1.3521,103.8198");
        String nextSort = call.queryString("next.sort", null);
        int size = call.querySize(20, 30);


        NextNodeList<ArticleFeedItem> items = articleFeedClient.get(country, latLng, nextSort, size);
        return result(items);
    }
}
