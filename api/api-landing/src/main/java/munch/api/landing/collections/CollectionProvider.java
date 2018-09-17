package munch.api.landing.collections;

import munch.restful.server.JsonCall;
import munch.user.data.UserPlaceCollection;

import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 17/9/18
 * Time: 3:04 PM
 * Project: munch-core
 */
@Singleton
public final class CollectionProvider {

    /**
     * @return
     */
    public List<UserPlaceCollection> get(JsonCall call) {
        return List.of();
    }
}
