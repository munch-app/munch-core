package munch.api.feed;

import munch.data.client.PlaceCachedClient;
import munch.feed.ImageFeedClient;
import munch.feed.ImageFeedItem;
import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.user.client.UserSavedPlaceClient;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 27/11/18
 * Time: 9:44 AM
 * Project: munch-core
 */
@Singleton
public final class FeedImageService extends FeedService {

    private final ImageFeedClient imageFeedClient;

    @Inject
    public FeedImageService(PlaceCachedClient placeClient, ImageFeedClient imageFeedClient, UserSavedPlaceClient savedPlaceClient) {
        super(placeClient, savedPlaceClient);
        this.imageFeedClient = imageFeedClient;
    }

    @Override
    public void route() {
        PATH("/feed/images", () -> {
            GET("", this::queryImages);
            GET("/:itemId", this::getImage);
        });
    }

    public JsonResult queryImages(JsonCall call) {
        String country = call.queryString("country", "sgp");
        String latLng = call.queryString("latLng", "1.3521,103.8198");

        int from = call.queryInt("next.from", 0);
        int size = call.querySize(20, 30);

        NextNodeList<ImageFeedItem> items = imageFeedClient.query(country, latLng, from, size);
        return result(items);
    }

    public JsonResult getImage(JsonCall call) {
        String itemId = call.pathString("itemId");
        ImageFeedItem feedItem = imageFeedClient.get(itemId);

        if (feedItem == null) return JsonResult.notFound();
        return asResult(call, feedItem);
    }
}
