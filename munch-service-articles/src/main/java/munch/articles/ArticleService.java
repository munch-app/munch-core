package munch.articles;

import com.google.inject.Singleton;
import munch.restful.server.JsonService;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 2:22 AM
 * Project: munch-core
 */
@Singleton
public class ArticleService implements JsonService {

    @Override
    public void route() {
        PATH("/articles", () -> {

        });
    }

}
