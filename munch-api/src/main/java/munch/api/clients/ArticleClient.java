package munch.api.clients;

import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.restful.client.RestfulClient;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by: Fuxing
 * Date: 7/6/2017
 * Time: 12:37 PM
 * Project: munch-core
 */
@Singleton
public class ArticleClient extends RestfulClient {

    @Inject
    public ArticleClient(@Named("services") Config config) {
        super(config.getString("articles.url"));
    }

    // TODO get and list method
}
