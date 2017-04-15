package munch.search;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.restful.server.RestfulServer;
import munch.restful.server.RestfulService;
import munch.search.elastic.ElasticMapping;
import munch.search.elastic.ElasticModule;

import java.io.IOException;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 16/4/2017
 * Time: 6:41 AM
 * Project: munch-core
 */
@Singleton
public final class SearchApi extends RestfulServer {

    @Inject
    public SearchApi(Set<RestfulService> routers) {
        super(routers);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new SearchModule(),
                new ElasticModule()
        );

        // Load elastic mapping and validate it
        try {
            ElasticMapping mapping = injector.getInstance(ElasticMapping.class);
            if (!mapping.validate())
                throw new RuntimeException("ElasticSearchMapping for /munch/place index validation failed.");
        } catch (IOException e) {
            throw new RuntimeException("ElasticSearchMapping failed", e);
        }

        // Start server on default port in setting = http.port
        Config config = injector.getInstance(Config.class);
        final int port = config.getInt("http.port");
        final RestfulServer server = injector.getInstance(SearchApi.class);
        server.start(port);
    }
}
