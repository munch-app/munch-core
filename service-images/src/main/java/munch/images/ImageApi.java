package munch.images;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.restful.server.RestfulServer;
import munch.restful.server.RestfulService;

import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 2:21 AM
 * Project: munch-core
 */
@Singleton
public class ImageApi extends RestfulServer {

    @Inject
    public ImageApi(Set<RestfulService> services) {
        super(services);
    }

}
