package munch.api.endpoints.service;

import com.typesafe.config.Config;
import munch.restful.client.RestfulClient;
import munch.struct.place.Place;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 5:06 PM
 * Project: munch-core
 */
@Singleton
public class DataClient extends RestfulClient {

    /**
     * Look at data service package to api service settings
     *
     * @param config config to load data.url
     */
    @Inject
    public DataClient(@Named("services") Config config) {
        super(config.getString("data.url"));
    }

    public Place get(String key) {
        return doGet("/places/id")
                .path("id", key)
                .asDataObject(Place.class);
    }

    public List<Place> get(List<String> keys) {
        return doGet("/places/batch/get")
                .body(keys)
                .asDataList(Place.class);
    }
}
