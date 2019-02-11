package munch.api.creator;

import munch.api.ApiRequest;
import munch.api.ApiService;
import munch.restful.core.exception.ForbiddenException;
import munch.restful.server.JsonCall;
import munch.user.client.CreatorContentClient;
import munch.user.client.CreatorSeriesClient;
import munch.user.client.CreatorUserClient;
import munch.user.data.CreatorContent;
import munch.user.data.CreatorSeries;
import munch.user.data.CreatorUser;
import spark.RouteGroup;

import javax.inject.Inject;

/**
 * This implementation is a little messy, should be rewritten.
 * <p>
 * Created by: Fuxing
 * Date: 2019-02-02
 * Time: 23:29
 * Project: munch-core
 */
public abstract class AbstractCreatorService extends ApiService {

    private CreatorUserClient creatorUserClient;
    private CreatorSeriesClient seriesClient;
    private CreatorContentClient contentClient;

    @Inject
    void inject(CreatorUserClient creatorUserClient, CreatorSeriesClient seriesClient, CreatorContentClient contentClient) {
        this.creatorUserClient = creatorUserClient;
        this.seriesClient = seriesClient;
        this.contentClient = contentClient;
    }

    /**
     * Authenticated route
     */
    protected void AUTHENTICATED(String path, RouteGroup routeGroup) {
        super.PATH(path, () -> {
            BEFORE("", this::before);
            BEFORE("/*", this::before);

            routeGroup.addRoutes();
        });
    }

    @Override
    @Deprecated
    public void PATH(String path, RouteGroup routeGroup) {
        throw new RuntimeException("Use AUTHENTICATED instead");
    }

    private void before(JsonCall call) {
        final String creatorId = call.pathString("creatorId");
        final String contentId = call.pathString("contentId", null);
        final String seriesId = call.pathString("seriesId", null);

        if (!creatorId.equals("_")) {
            authenticateUser(call, creatorId);
        }

        if (contentId != null && !contentId.equals("_")) {
            authenticateContent(call, creatorId, contentId);
        }

        if (seriesId != null) {
            authenticateSeries(call, creatorId, seriesId);
        }
    }

    private void authenticateUser(JsonCall call, String creatorId) {
        if (call.get(CreatorUser.class) != null) return;

        final ApiRequest request = call.get(ApiRequest.class);
        final String userId = request.getUserId();

        CreatorUser user = creatorUserClient.get(creatorId, userId);
        if (user == null) throw new ForbiddenException("Creator Forbidden");
        call.put(user, CreatorUser.class);
    }

    @SuppressWarnings("Duplicates")
    private void authenticateContent(JsonCall call, String creatorId, String contentId) {
        if (call.get(CreatorContent.class) != null) return;

        CreatorContent content = contentClient.get(contentId);
        if (content == null) throw new ForbiddenException("Creator Forbidden");

        if (creatorId.equals("_")) {
            authenticateUser(call, content.getCreatorId());
        } else {
            if (!content.getCreatorId().equals(creatorId)) throw new ForbiddenException("Creator Forbidden");
        }

        call.put(content, CreatorContent.class);
    }

    @SuppressWarnings("Duplicates")
    private void authenticateSeries(JsonCall call, String creatorId, String seriesId) {
        if (call.get(CreatorSeries.class) != null) return;

        CreatorSeries series = seriesClient.get(seriesId);
        if (series == null) throw new ForbiddenException("Creator Forbidden");

        if (creatorId.equals("_")) {
            authenticateUser(call, series.getCreatorId());
        } else {
            if (!series.getCreatorId().equals(creatorId)) throw new ForbiddenException("Creator Forbidden");
        }

        call.put(series, CreatorSeries.class);
    }
}
