package munch.api.clients;

import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.restful.client.RestfulClient;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by: Fuxing
 * Date: 31/5/2017
 * Time: 5:37 AM
 * Project: munch-core
 */
@Singleton
public class GalleryClient extends RestfulClient {

    @Inject
    public GalleryClient(@Named("services") Config config) {
        super(config.getString("gallery.url"));
    }

    // TODO get and list method
}
