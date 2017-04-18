package munch.articles;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.typesafe.config.ConfigFactory;
import munch.restful.server.RestfulServer;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 2:21 AM
 * Project: munch-core
 */
@Singleton
public class ArticleApi extends RestfulServer {

    @Inject
    public ArticleApi(ArticleService service) {
        super(service);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new ArticleModule()
        );

        // Start server on default port in setting = http.port
        final RestfulServer server = injector.getInstance(ArticleApi.class);
        server.start(ConfigFactory.load().getInt("http.port"));
    }
}
