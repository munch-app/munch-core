package munch.api.creator;

import munch.api.ApiRequest;
import munch.api.ApiService;
import munch.restful.core.exception.ForbiddenException;
import munch.restful.server.JsonCall;
import munch.user.client.CreatorSeriesClient;
import munch.user.client.CreatorStoryClient;
import munch.user.client.CreatorUserClient;
import munch.user.data.CreatorSeries;
import munch.user.data.CreatorStory;
import munch.user.data.CreatorUser;

import javax.inject.Inject;

/**
 * Created by: Fuxing
 * Date: 2019-02-02
 * Time: 23:29
 * Project: munch-core
 */
public abstract class AbstractCreatorService extends ApiService {

    private CreatorUserClient creatorUserClient;
    private CreatorSeriesClient seriesClient;
    private CreatorStoryClient storyClient;

    @Inject
    void inject(CreatorUserClient creatorUserClient, CreatorSeriesClient seriesClient, CreatorStoryClient storyClient) {
        this.creatorUserClient = creatorUserClient;
        this.seriesClient = seriesClient;
        this.storyClient = storyClient;
    }

    /**
     * @param call JsonCall to validate that creatorId is authenticated
     */
    protected void authenticateCreator(JsonCall call) {
        final ApiRequest request = call.get(ApiRequest.class);
        final String userId = request.getUserId();
        final String creatorId = call.pathString("creatorId");

        CreatorUser user = creatorUserClient.get(creatorId, userId);
        if (user == null) throw new ForbiddenException("creator");

        // Add CreatorUser to global session
        call.put(user, CreatorUser.class);
    }

    protected void authenticateSeries(JsonCall call) {
        final String creatorId = call.pathString("creatorId");
        final String seriesId = call.pathString("seriesId");

        CreatorSeries series = seriesClient.get(seriesId);
        if (series == null) throw new ForbiddenException("creator");
        if (!series.getCreatorId().equals(creatorId)) throw new ForbiddenException("creator");
    }

    protected void authenticateStory(JsonCall call) {
        final String creatorId = call.pathString("creatorId");
        final String storyId = call.pathString("storyId");

        CreatorStory story = storyClient.get(storyId);
        if (story == null) throw new ForbiddenException("creator");
        if (!story.getCreatorId().equals(creatorId)) throw new ForbiddenException("creator");
    }
}
