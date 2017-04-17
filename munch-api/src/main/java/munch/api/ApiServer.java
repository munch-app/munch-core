package munch.api;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.api.endpoints.EndpointsModule;
import munch.api.endpoints.service.ServicesModule;
import munch.data.PostgresModule;
import munch.restful.server.RestfulServer;
import munch.restful.server.RestfulService;
import munch.search.elastic.ElasticModule;

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
        handleException();
    }

    /**
     * Handle exceptions
     */
    private void handleException() {
        // TODO handle exceptions
    }

    /**
     * Start api server with predefined modules
     *
     * @param args not required
     */
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new ApiModule(),
                new PostgresModule(),
                new ElasticModule(),
                new EndpointsModule(),
                new ServicesModule()
        );

        Config config = injector.getInstance(Config.class);
        final int port = config.getInt("http.port");
        final RestfulServer server = injector.getInstance(ApiServer.class);

        // Start api server on port.
        server.start(port);
    }
}
