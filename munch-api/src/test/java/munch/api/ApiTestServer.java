package munch.api;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import munch.api.endpoints.AbstractEndpoint;
import org.fest.util.Collections;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 9:53 PM
 * Project: munch-core
 */
public interface ApiTestServer {

    int DEFAULT_PORT = 8888;

    /**
     * @param modules created modules to bind
     * @return created Injector
     */
    static Injector injector(AbstractModule... modules) {
        return Guice.createInjector(modules);
    }

    static <T extends AbstractEndpoint> void start(Class<T> type, AbstractModule... modules) {
        Injector injector = injector(modules);
        T endpoint = injector.getInstance(type);

        ApiServer apiServer = new ApiServer(Collections.set(endpoint));
        apiServer.start(DEFAULT_PORT);
    }

}
