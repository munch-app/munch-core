package munch.api.creator;

import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;
import munch.user.client.CreatorSeriesClient;
import munch.user.client.CreatorSeriesStoryClient;
import munch.user.data.CreatorSeries;
import munch.user.data.CreatorSeriesStory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 2019-02-02
 * Time: 23:03
 * Project: munch-core
 */
@Singleton
public final class CreatorSeriesService extends AbstractCreatorService {

    private final CreatorSeriesClient seriesClient;
    private final CreatorSeriesStoryClient seriesStoryClient;

    @Inject
    public CreatorSeriesService(CreatorSeriesClient seriesClient, CreatorSeriesStoryClient seriesStoryClient) {
        this.seriesClient = seriesClient;
        this.seriesStoryClient = seriesStoryClient;
    }

    @Override
    public void route() {
        PATH("/creators/:creatorId/series", () -> {
            BEFORE("", this::authenticateCreator);

            GET("", this::list);
            POST("", this::post);

            PATH("/:seriesId", () -> {
                BEFORE("", this::authenticateSeries);

                PATCH("", this::patch);
                DELETE("", this::delete);

                PATH("/stories", () -> {
                    GET("", this::listStory);

                    BEFORE("/:storyId", this::authenticateStory);
                    POST("/:storyId", this::postStory);
                    PATCH("/:storyId", this::patchStory);
                    DELETE("/:storyId", this::deleteStory);
                });
            });
        });
    }

    @SuppressWarnings("Duplicates")
    public NextNodeList<CreatorSeries> list(JsonCall call) {
        final String creatorId = call.pathString("creatorId");
        final int size = call.querySize(20, 40);

        CreatorSeriesClient.ListMethod method = call.queryEnum("sort", CreatorSeriesClient.ListMethod.class, CreatorSeriesClient.ListMethod.sort);
        String next = call.queryString("next." + method.nextName(), null);
        return seriesClient.list(method, creatorId, next, size);
    }

    public CreatorSeries post(JsonCall call) {
        CreatorSeries series = call.bodyAsObject(CreatorSeries.class);
        series.setCreatorId(call.pathString("creatorId"));
        return seriesClient.post(series);
    }

    public CreatorSeries patch(JsonCall call) {
        String creatorId = call.pathString("creatorId");
        String seriesId = call.pathString("seriesId");

        return seriesClient.patch(creatorId, seriesId, call.bodyAsJson());
    }

    public CreatorSeries delete(JsonCall call) {
        String creatorId = call.pathString("creatorId");
        String seriesId = call.pathString("seriesId");
        return seriesClient.delete(creatorId, seriesId);
    }

    public NextNodeList<CreatorSeriesStory> listStory(JsonCall call) {
        final String seriesId = call.pathString("seriesId");
        final int size = call.querySize(20, 40);

        CreatorSeriesStoryClient.ListMethod method = call.queryEnum("sort", CreatorSeriesStoryClient.ListMethod.class, CreatorSeriesStoryClient.ListMethod.sort);
        String next = call.queryString("next." + method.nextName(), null);
        return seriesStoryClient.list(method, seriesId, next, size);
    }

    public CreatorSeriesStory postStory(JsonCall call) {
        CreatorSeriesStory seriesStory = call.bodyAsObject(CreatorSeriesStory.class);
        seriesStory.setSeriesId(call.pathString("seriesId"));
        seriesStory.setStoryId(call.pathString("storyId"));
        return seriesStoryClient.post(seriesStory);
    }

    public CreatorSeriesStory patchStory(JsonCall call) {
        final String seriesId = call.pathString("seriesId");
        final String storyId = call.pathString("storyId");
        return seriesStoryClient.patch(seriesId, storyId, call.bodyAsJson());
    }

    public CreatorSeriesStory deleteStory(JsonCall call) {
        final String seriesId = call.pathString("seriesId");
        final String storyId = call.pathString("storyId");
        return seriesStoryClient.delete(seriesId, storyId);
    }
}
