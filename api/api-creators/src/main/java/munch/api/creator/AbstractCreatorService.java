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
    private static boolean initialised = false;

    private CreatorUserClient creatorUserClient;
    private CreatorSeriesClient seriesClient;
    private CreatorContentClient contentClient;

    @Inject
    void inject(CreatorUserClient creatorUserClient, CreatorSeriesClient seriesClient, CreatorContentClient contentClient) {
        this.creatorUserClient = creatorUserClient;
        this.seriesClient = seriesClient;
        this.contentClient = contentClient;
    }

    @Override
    public void start() {
        if (initialised) return;

        BEFORE("/creators/*", this::before);
        super.start();

        initialised = true;
    }

    private void before(JsonCall call) {
        final String creatorId = call.pathString("creatorId");
        final String contentId = call.pathString("contentId", null);
        final String seriesId = call.pathString("seriesId", null);

        if (!creatorId.equals("_")) {
            authenticateUser(call, creatorId);
        }

        if (contentId != null) {
            authenticateContent(call, creatorId, contentId);
        }

        if (seriesId != null) {
            authenticateSeries(call, creatorId, seriesId);
        }
    }

    private void authenticateUser(JsonCall call, String creatorId) {
        final ApiRequest request = call.get(ApiRequest.class);
        final String userId = request.getUserId();

        CreatorUser user = creatorUserClient.get(creatorId, userId);
        if (user == null) throw new ForbiddenException("Creator Forbidden");
        call.put(user, CreatorUser.class);
    }

    private void authenticateContent(JsonCall call, String creatorId, String contentId) {
        CreatorContent content = contentClient.get(contentId);
        if (content == null) throw new ForbiddenException("Creator Forbidden");

        if (creatorId.equals("_")) {
            authenticateUser(call, content.getCreatorId());
        } else {
            if (!content.getCreatorId().equals(creatorId)) throw new ForbiddenException("Creator Forbidden");
        }

        call.put(content, CreatorContent.class);
    }

    private void authenticateSeries(JsonCall call, String creatorId, String seriesId) {
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
