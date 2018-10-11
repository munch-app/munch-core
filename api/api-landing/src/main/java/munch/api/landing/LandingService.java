package munch.api.landing;

import munch.api.ApiService;
import munch.api.landing.collections.CollectionProvider;
import munch.restful.server.JsonCall;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 17/9/18
 * Time: 3:01 PM
 * Project: munch-core
 */
@Singleton
public final class LandingService extends ApiService {

    private final CollectionProvider collectionProvider;

    @Inject
    public LandingService(CollectionProvider collectionProvider) {
        this.collectionProvider = collectionProvider;
    }

    @Override
    public void route() {
        PATH("/landing", () -> {
            POST("", this::post);
        });
    }

    /**
     * @return Landing page data
     */
    public Map<String, Object> post(JsonCall call) {
        LandingRequest request = new LandingRequest(call);
        return Map.of(
//                "locations", null,
//                "queries", null,
                "collections", collectionProvider.get(request)
        );
    }
}
