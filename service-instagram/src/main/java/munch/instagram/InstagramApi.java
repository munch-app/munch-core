package munch.instagram;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.restful.server.RestfulServer;

/**
 * Created by: Fuxing
 * Date: 3/7/2017
 * Time: 11:24 PM
 * Project: munch-core
 */
@Singleton
final class InstagramApi extends RestfulServer {
    @Inject
    public InstagramApi(MediaService service) {
        super(service);
    }
}
