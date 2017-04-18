package munch.images;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.typesafe.config.ConfigFactory;
import munch.restful.server.RestfulServer;
import munch.restful.server.RestfulService;

import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 2:21 AM
 * Project: munch-core
 */
@Singleton
public class ImageApi extends RestfulServer {

    @Inject
    public ImageApi(Set<RestfulService> services) {
        super(services);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new ImageModule()
        );

        // Start server on default port in setting = http.port
        final RestfulServer server = injector.getInstance(ImageApi.class);
        server.start(ConfigFactory.load().getInt("http.port"));
    }
}
