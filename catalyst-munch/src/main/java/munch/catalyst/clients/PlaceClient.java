package munch.catalyst.clients;

import munch.catalyst.data.Place;
import munch.restful.client.RestfulClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Date;

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
     */
    @Inject
    public PlaceClient(@Named("services.places.url") String url) {
        super(url);
    }

    public void put(Place place) {
        doPut("/places/{id}")
                .path("id", place.getId())
                .body(place)
                .asResponse()
                .hasCode(200);
    }

    public void deleteBefore(Date updatedDate) {
        doDelete("/places/before/{timestamp}")
                .path("timestamp", updatedDate.getTime())
                .asResponse()
                .hasCode(200);
    }
}
