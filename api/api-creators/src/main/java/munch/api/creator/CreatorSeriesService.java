package munch.api.creator;

import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;
import munch.user.client.CreatorSeriesClient;
import munch.user.client.CreatorSeriesContentClient;
import munch.user.data.CreatorSeries;
import munch.user.data.CreatorSeriesContent;
import munch.user.data.CreatorSeriesContentIndex;
import munch.user.data.CreatorSeriesIndex;

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
    private final CreatorSeriesContentClient seriesContentClient;

    @Inject
    public CreatorSeriesService(CreatorSeriesClient seriesClient, CreatorSeriesContentClient seriesContentClient) {
        this.seriesClient = seriesClient;
        this.seriesContentClient = seriesContentClient;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void route() {
        PATH("/creators/:creatorId/series", () -> {
            GET("", this::list);
            POST("", this::post);

            PATH("/:seriesId", () -> {
                GET("", this::get);
                PATCH("", this::patch);
                DELETE("", this::delete);
                PATH("/contents", () -> {
                    GET("", this::listContent);

                    POST("/:contentId", this::postContent);
                    PATCH("/:contentId", this::patchContent);
                    DELETE("/:contentId", this::deleteContent);
                });
            });
        });
    }

    @SuppressWarnings("Duplicates")
    public NextNodeList<CreatorSeries> list(JsonCall call) {
        final String creatorId = call.pathString("creatorId");
        final int size = call.querySize(20, 40);

        CreatorSeriesIndex index = call.queryEnum("index", CreatorSeriesIndex.class, CreatorSeriesIndex.sortId);
        String next = call.queryString("next." + index.getRangeName(), null);
        return seriesClient.list(index, creatorId, next, size);
    }

    public CreatorSeries get(JsonCall call) {
        return call.get(CreatorSeries.class);
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

    public NextNodeList<CreatorSeriesContent> listContent(JsonCall call) {
        final String seriesId = call.pathString("seriesId");
        final int size = call.querySize(20, 40);

        CreatorSeriesContentIndex index = call.queryEnum("index", CreatorSeriesContentIndex.class, CreatorSeriesContentIndex.sortId);
        String next = call.queryString("next." + index.getRangeName(), null);
        return seriesContentClient.list(index, seriesId, next, size);
    }

    public CreatorSeriesContent postContent(JsonCall call) {
        CreatorSeriesContent seriesContent = call.bodyAsObject(CreatorSeriesContent.class);
        seriesContent.setSeriesId(call.pathString("seriesId"));
        seriesContent.setContentId(call.pathString("contentId"));
        return seriesContentClient.post(seriesContent);
    }

    public CreatorSeriesContent patchContent(JsonCall call) {
        final String seriesId = call.pathString("seriesId");
        final String contentId = call.pathString("contentId");
        return seriesContentClient.patch(seriesId, contentId, call.bodyAsJson());
    }

    public CreatorSeriesContent deleteContent(JsonCall call) {
        final String seriesId = call.pathString("seriesId");
        final String contentId = call.pathString("contentId");
        return seriesContentClient.delete(seriesId, contentId);
    }
}
