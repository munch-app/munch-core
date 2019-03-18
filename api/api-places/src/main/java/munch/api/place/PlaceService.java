package munch.api.place;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.ApiRequest;
import munch.api.ApiService;
import munch.article.ArticleLinkClient;
import munch.article.data.ArticleSection;
import munch.data.client.PlaceClient;
import munch.data.place.Place;
import munch.gallery.PlaceImage;
import munch.gallery.PlaceImageClient;
import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.taste.PlaceRatingClient;
import munch.user.client.AwardCollectionClient;
import munch.user.client.UserRatedPlaceClient;
import munch.user.client.UserSavedPlaceClient;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 2/4/2017
 * Time: 4:15 PM
 * Project: munch-core
 */
@Singleton
public final class PlaceService extends ApiService {

    private final PlaceClient placeClient;

    private final ArticleLinkClient articleLinkClient;
    private final AwardCollectionClient awardCollectionClient;

    private final PlaceImageClient placeImageClient;

    private final UserSavedPlaceClient savedPlaceClient;
    private final UserRatedPlaceClient ratedPlaceClient;

    private final PlaceRatingClient placeRatingClient;

    @Inject
    public PlaceService(PlaceClient placeClient, ArticleLinkClient articleLinkClient, AwardCollectionClient awardCollectionClient, PlaceImageClient placeImageClient, UserSavedPlaceClient savedPlaceClient, UserRatedPlaceClient ratedPlaceClient, PlaceRatingClient placeRatingClient) {
        this.placeClient = placeClient;
        this.articleLinkClient = articleLinkClient;
        this.awardCollectionClient = awardCollectionClient;
        this.placeImageClient = placeImageClient;
        this.savedPlaceClient = savedPlaceClient;
        this.ratedPlaceClient = ratedPlaceClient;
        this.placeRatingClient = placeRatingClient;
    }

    enum Linked {
        awards,
        articles,
        images,
        user,
        rating,
        ;

        static Set<Linked> DEFAULT = Set.of(
                awards, articles, images, user, rating
        );

        static Set<Linked> map(JsonCall call) {
            String linked = call.queryString("linked", null);
            if (linked == null) return DEFAULT;

            if (StringUtils.isBlank(linked)) return Set.of();

            return Arrays.stream(linked.split(","))
                    .filter(StringUtils::isNotBlank)
                    .map(t -> EnumUtils.getEnum(Linked.class, t))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        }
    }

    /**
     * Endpoint: /v/places/:placeId
     */
    @Override
    public void route() {
        PATH("/places/:placeId", () -> {
            GET("", this::get);
            GET("/images", this::getImages);
            GET("/articles", this::getArticles);
        });
    }

    /**
     * GET = /places/:placeId
     *
     * @param call json call
     * @return Place or Null
     */
    private JsonResult get(JsonCall call) {
        String placeId = call.pathString("placeId");
        Place place = placeClient.get(placeId);
        if (place == null) return JsonResult.notFound();

        Set<Linked> linked = Linked.map(call);
        Map<String, Object> map = new HashMap<>();
        map.put("place", place);

        if (linked.contains(Linked.awards)) {
            map.put("awards", awardCollectionClient.list(placeId, null, 10));
        }

        if (linked.contains(Linked.articles)) {
            map.put("articles", articleLinkClient.list(placeId, null, 10));
        }

        if (linked.contains(Linked.images)) {
            map.put("images", placeImageClient.list(placeId, null, 10));
        }

        if (linked.contains(Linked.user)) {
            map.put("user", getUser(call, placeId));
        }

        if (linked.contains(Linked.rating)) {
            map.put("rating", placeRatingClient.get(placeId));
        }

        return JsonResult.ok(map);
    }

    private Map<String, Object> getUser(JsonCall call, String placeId) {
        ApiRequest request = call.get(ApiRequest.class);
        if (!request.optionalUserId().isPresent()) return Map.of();

        String userId = request.optionalUserId().get();

        Map<String, Object> user = new HashMap<>();
        user.put("savedPlace", savedPlaceClient.get(userId, placeId));
        user.put("ratedPlace", ratedPlaceClient.get(userId, placeId));
        return user;
    }

    /**
     * @return all linked images
     */
    public NextNodeList<PlaceImage> getImages(JsonCall call) {
        String placeId = call.pathString("placeId");
        String nextSort = call.queryString("next.sort", null);
        int size = call.querySize(20, 40);

        return placeImageClient.list(placeId, nextSort, size);
    }

    private NextNodeList<ArticleSection> getArticles(JsonCall call) {
        int size = call.querySize(20, 40);
        String placeId = call.pathString("placeId");
        String nextSort = call.queryString("next.sort", null);

        return articleLinkClient.list(placeId, nextSort, size);
    }
}
