package munch.articles;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.restful.server.RestfulServer;
import spark.Spark;

/**
 * Created by: Fuxing
 * Date: 3/7/2017
 * Time: 11:25 PM
 * Project: munch-core
 */
@Singleton
public class ArticleApi extends RestfulServer {
    @Inject
    public ArticleApi(ArticleService service) {
        super(service);
    }

    @Override
    public void start(int port) {
        super.start(port);
        Spark.before((request, response) -> logger.info("{}: {}", request.requestMethod(), request.pathInfo()));
    }
}
