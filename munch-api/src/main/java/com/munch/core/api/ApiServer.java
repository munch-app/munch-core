package com.munch.core.api;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import munch.restful.server.RestfulService;
import munch.restful.server.RestfulServer;
import com.typesafe.config.Config;

import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 3:18 AM
 * Project: munch-core
 */
@Singleton
public final class ApiServer extends RestfulServer {

    @Inject
    public ApiServer(Set<RestfulService> routers) {
        super(routers);
    }

    @Override
    public void start(int port) {
        super.start(port);
    }

    /**
     * Start api server with predefined modules
     *
     * @param args not required
     */
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
        );

        Config config = injector.getInstance(Config.class);
        final int port = config.getInt("http.port");
        final RestfulServer server = injector.getInstance(ApiServer.class);

        // Start api server on port.
        server.start(port);
    }
}
