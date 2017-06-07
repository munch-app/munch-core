package munch.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.restful.server.RestfulServer;
import munch.restful.server.RestfulService;
import spark.Spark;

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
        Spark.before((request, response) -> logger.info("{}: {}", request.requestMethod(), request.pathInfo()));
    }
}
