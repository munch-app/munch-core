package munch.api.services;

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
public class ImageClient extends RestfulClient {

    @Inject
    public ImageClient(@Named("services") Config config) {
        super(config.getString("images.url"));
    }

}
