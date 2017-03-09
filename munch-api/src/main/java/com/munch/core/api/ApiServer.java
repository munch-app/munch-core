package com.munch.core.api;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.munch.utils.spark.SparkRouter;
import com.munch.utils.spark.SparkServer;
import com.typesafe.config.Config;

import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 3:18 AM
 * Project: munch-core
 */
@Singleton
public final class ApiServer extends SparkServer {

    @Inject
    public ApiServer(Set<SparkRouter> routers) {
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
        final SparkServer server = injector.getInstance(ApiServer.class);

        // Start api server on port.
        server.start(port);
    }
}
