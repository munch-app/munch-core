package munch.api.place;

import munch.api.ApiRequest;
import munch.api.ApiService;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.suggest.SuggestClient;
import munch.suggest.SuggestPlace;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 5/11/18
 * Time: 1:43 PM
 * Project: munch-core
 */
@Singleton
public final class SuggestPlaceService extends ApiService {

    private final SuggestClient suggestClient;

    @Inject
    public SuggestPlaceService(SuggestClient suggestClient) {
        this.suggestClient = suggestClient;
    }

    @Override
    public void route() {
        PATH("/suggests/places", () -> {
            POST("", this::suggest);
        });
    }

    public JsonResult suggest(JsonCall call) {
        ApiRequest request = call.get(ApiRequest.class);

        SuggestPlace suggestPlace = call.bodyAsObject(SuggestPlace.class);
        suggestPlace.setUserId(request.getUserId());

        suggestClient.post(suggestPlace);
        return JsonResult.ok();
    }
}
