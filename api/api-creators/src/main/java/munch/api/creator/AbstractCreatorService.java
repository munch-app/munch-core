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
     * if creatorId is _ the resources must do the authentication
     *
     * @return whether authentication is required, creatorId:_, the resources will authenticate instead.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean requireAuthentication(JsonCall call, String... names) {
        for (String name : names) {
            final String value = call.pathString(name);
            if (value.equals("_")) return false;
        }

        return true;
    }

    /**
     * if creatorId is _ the resources must do the authentication
     *
     * @param call JsonCall to validate that creatorId is authenticated
     */
    protected void authenticateCreator(JsonCall call) {
        if (!requireAuthentication(call, "creatorId")) return;

        authenticateCreator(call, call.pathString("creatorId"));
    }

    protected void authenticateCreator(JsonCall call, String creatorId) {
        final ApiRequest request = call.get(ApiRequest.class);
        final String userId = request.getUserId();

        // Check if already authenticated
        if (call.get(CreatorUser.class) != null) return;

        CreatorUser user = creatorUserClient.get(creatorId, userId);
        if (user == null) throw new ForbiddenException("Creator Forbidden");

        // Add CreatorUser to session for future access
        call.put(user, CreatorUser.class);
    }

    protected void authenticateSeries(JsonCall call) {
        if (!requireAuthentication(call, "creatorId")) return;

        final String creatorId = call.pathString("creatorId");
        final String seriesId = call.pathString("seriesId");

        // Check if already authenticated
        if (call.get(CreatorSeries.class) != null) return;

        CreatorSeries series = seriesClient.get(seriesId);
        if (series == null) throw new ForbiddenException("creator");
        if (!series.getCreatorId().equals(creatorId)) throw new ForbiddenException("Creator Forbidden");

        // Add CreatorSeries to session for future access
        call.put(series, CreatorSeries.class);

    }

    protected void authenticateContent(JsonCall call) {
        if (!requireAuthentication(call, "creatorId", "contentId")) return;

        final String creatorId = call.pathString("creatorId");
        final String contentId = call.pathString("contentId");

        // Check if already authenticated
        if (call.get(CreatorContent.class) != null) return;

        CreatorContent content = contentClient.get(contentId);
        if (content == null) throw new ForbiddenException("creator");
        if (!content.getCreatorId().equals(creatorId)) throw new ForbiddenException("Creator Forbidden");

        // Add CreatorContent to session for future access
        call.put(content, CreatorContent.class);
    }
}
