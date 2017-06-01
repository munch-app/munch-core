package munch.catalyst.clients;

import com.typesafe.config.Config;
import munch.restful.client.RestfulClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 9:44 PM
 * Project: munch-core
 */
@Singleton
public class PlaceClient extends RestfulClient {

    /**
     * Look at data service package to api service settings
     *
     * @param config config to load data.url
     */
    @Inject
    public PlaceClient(@Named("services") Config config) {
        super(config.getString("places.url"));
    }

    public void put(Place place) {
        // TODO restful
    }

    public void delete(String catalystId) {
        // TODO delete all place with catalystId
    }

    public static class Place {
        private String id;

        // TODO data
    }
}
