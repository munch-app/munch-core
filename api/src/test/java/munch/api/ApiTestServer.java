package munch.api;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import munch.restful.server.RestfulServer;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 9:53 PM
 * Project: munch-core
 */
public interface ApiTestServer {

    /**
     * @param modules created modules to bind
     * @return created Injector
     */
    static Injector injector(AbstractModule... modules) {
        return Guice.createInjector(modules);
    }

    static <T extends ApiService> void start(Class<T> type, AbstractModule... modules) {
        System.setProperty("http.port", "8888");

        Injector injector = injector(modules);
        RestfulServer.start(injector.getInstance(type));
    }

}
