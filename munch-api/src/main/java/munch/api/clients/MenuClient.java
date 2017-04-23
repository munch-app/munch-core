package munch.api.clients;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.restful.client.RestfulClient;

import javax.inject.Named;

/**
 * Created by: Fuxing
 * Date: 22/4/2017
 * Time: 9:47 PM
 * Project: munch-core
 */
@Singleton
public class MenuClient extends RestfulClient {

    @Inject
    public MenuClient(@Named("services") Config config) {
        super(config.getString("places-menu.url"));
    }

}
