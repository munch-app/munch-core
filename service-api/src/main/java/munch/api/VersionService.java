package munch.api;

import munch.restful.server.JsonService;

import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 15/9/2017
 * Time: 8:34 PM
 * Project: munch-core
 */
@Singleton
public final class VersionService implements JsonService {

    @Override
    public void route() {
        GET("/versions/validate", call -> nodes(200));
    }

}
