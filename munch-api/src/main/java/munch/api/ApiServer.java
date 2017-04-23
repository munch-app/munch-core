package munch.api;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.typesafe.config.ConfigFactory;
import munch.api.services.ServiceModule;
import munch.api.clients.ClientModule;
import munch.restful.server.RestfulServer;
import munch.restful.server.RestfulService;

import java.util.Set;

/**
 * FYI: For error handling use Structured Error
 * <p>
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
                new ApiModule(),
                new ClientModule(),
                new ServiceModule()
        );

        // Start api server on port on http.port
        final RestfulServer server = injector.getInstance(ApiServer.class);
        server.start(ConfigFactory.load().getInt("http.port"));
    }
}
