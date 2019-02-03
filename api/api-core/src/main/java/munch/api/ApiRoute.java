package munch.api;

import munch.restful.server.JsonCall;
import munch.restful.server.JsonRoute;

/**
 * Created by: Fuxing
 * Date: 2019-02-02
 * Time: 22:06
 * Project: munch-core
 */
@FunctionalInterface
public interface ApiRoute extends JsonRoute {

    Object handle(JsonCall call, ApiRequest request);

    @Override
    default Object handle(JsonCall call) throws Exception {
        return handle(call, call.get(ApiRequest.class));
    }
}
