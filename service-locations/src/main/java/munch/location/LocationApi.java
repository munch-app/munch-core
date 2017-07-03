package munch.location;

import com.google.inject.Inject;
import munch.restful.server.RestfulServer;

import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 3/7/2017
 * Time: 11:24 PM
 * Project: munch-core
 */
@Singleton
public final class LocationApi extends RestfulServer {
    @Inject
    public LocationApi(LocationService service) {
        super(service);
    }
}
