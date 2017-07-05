package munch.places;

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
final class PlaceApi extends RestfulServer {

    @Inject
    public PlaceApi(PlaceService data, MetaService meta) {
        super(data, meta);
    }
}
