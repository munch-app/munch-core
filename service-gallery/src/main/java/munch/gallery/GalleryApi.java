package munch.gallery;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.typesafe.config.ConfigFactory;
import munch.restful.server.RestfulServer;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 6:58 PM
 * Project: munch-core
 */
@Singleton
public class GalleryApi extends RestfulServer {

    @Inject
    public GalleryApi(GalleryService service) {
        super(service);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new GalleryModule()
        );

        // Start server on default port in setting = http.port
        final RestfulServer server = injector.getInstance(GalleryApi.class);
        server.start(ConfigFactory.load().getInt("http.port"));
    }
}