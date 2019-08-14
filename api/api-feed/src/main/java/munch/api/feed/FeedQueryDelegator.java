package munch.api.feed;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.munch.api.ApiRequest;
import munch.data.elastic.ElasticUtils;
import munch.feed.FeedElasticClient;
import munch.feed.FeedElasticUtils;
import munch.feed.FeedItem;
import munch.restful.core.JsonUtils;
import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 2019-02-23
 * Time: 17:03
 * Project: munch-core
 */
@Singleton
public final class FeedQueryDelegator {

    private final FeedElasticClient elasticClient;

    @Inject
    public FeedQueryDelegator(FeedElasticClient elasticClient) {
        this.elasticClient = elasticClient;
    }

    public NextNodeList<FeedItem> query(FeedQuery feedQuery, JsonCall call, ApiRequest request) {
        final int size = call.querySize(30, 50);
        final int from = call.queryInt("next.from", 0);

        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", from);
        root.put("size", size);
        root.set("query", makeQuery(feedQuery, request));

        List<FeedItem> items = elasticClient.searchHitsHits(root);
        if (items.size() == size) {
            return new NextNodeList<>(items, "from", from + size);
        }
        return new NextNodeList<>(items);
    }

    private JsonNode makeQuery(FeedQuery feedQuery, ApiRequest request) {
        ObjectNode bool = JsonUtils.createObjectNode();
        bool.set("must", makeMust(feedQuery, request));
        bool.set("filter", makeFilter(feedQuery, request));
        return JsonUtils.wrap("bool", bool);
    }

    private JsonNode makeMust(FeedQuery feedQuery, ApiRequest request) {
        String latLng = getQueryLatLng(feedQuery);
        if (latLng != null) {
            return withFunctionScoreDecay(latLng, Duration.ofHours(2));
        }
        return FeedElasticUtils.withFunctionScoreRandom(Duration.ofHours(2));
    }

    private JsonNode makeFilter(FeedQuery feedQuery, ApiRequest request) {
        ArrayNode filters = JsonUtils.createArrayNode();
        filters.add(ElasticUtils.filterTerm("type", FeedItem.Type.InstagramMedia.name()));

        // All content must be within 600 days of creation
        long after = System.currentTimeMillis() - Duration.ofDays(600).toMillis();
        filters.add(ElasticUtils.filterRange("createdMillis", "gt", after));
        filters.add(getLocationFilter(feedQuery, request));
        return filters;
    }

    @Nullable
    private static String getQueryLatLng(FeedQuery feedQuery) {
        FeedQuery.Location location = feedQuery.getLocation();
        if (location == null) return null;
        return location.getLatLng();
    }

    private static JsonNode getLocationFilter(FeedQuery feedQuery, ApiRequest request) {
        // Given Location Filter
        String latLng = getQueryLatLng(feedQuery);
        if (latLng != null) {
            return ElasticUtils.filterDistance("latLng", latLng, 10_000);
        }


        // Implicit Location Filter
        if (request.getLatLng() != null) {
            return ElasticUtils.filterDistance("latLng", request.getLatLng(), 25_000);
        }


        // Regional Filter
        return ElasticUtils.filterTerm("country", "sgp");
    }


    public static JsonNode withFunctionScoreDecay(String latLng, Duration duration) {
        ObjectNode root = JsonUtils.createObjectNode();
        ObjectNode function = root.putObject("function_score");
        function.put("score_mode", "multiply");
        function.set("query", ElasticUtils.mustMatchAll());

        ArrayNode functions = function.putArray("functions");
        functions.addObject()
                .putObject("gauss")
                .putObject("latLng")
                .put("scale", "2km")
                .put("origin", latLng);

        functions.addObject()
                .putObject("random_score")
                .put("seed", System.currentTimeMillis() / duration.toMillis())
                .put("field", "_seq_no");
        return root;
    }
}
