package munch.catalyst.service;

import com.typesafe.config.Config;
import munch.restful.client.RestfulClient;
import munch.struct.place.Place;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

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

    @Nullable
    public PostgresPlace latest() {
        return doGet("/places/meta/last")
                .hasMetaCodes(200, 404)
                .asDataObject(PostgresPlace.class);
    }

    public void put(List<Place> places) {
        if (places.isEmpty()) return;
        doPost("/places/batch/put")
                .body(places)
                .hasMetaCodes(200);
    }

    public void delete(List<String> keys) {
        if (keys.isEmpty()) return;
        doPost("/places/batch/delete")
                .body(keys)
                .hasMetaCodes(200);
    }

}
