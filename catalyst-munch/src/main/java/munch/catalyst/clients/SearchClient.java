package munch.catalyst.clients;

import com.google.inject.Singleton;
import munch.catalyst.data.Location;
import munch.catalyst.data.Place;
import munch.restful.client.RestfulClient;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.function.Function;

/**
 * Created by: Fuxing
 * Date: 9/7/2017
 * Time: 4:23 AM
 * Project: munch-core
 */
@Singleton
public class SearchClient extends RestfulClient {

    @Inject
    public SearchClient(@Named("services.search.url") String url) {
        super(url);
    }

    public void put(Place place) {
        put("places", place, Place::getId);
    }

    public void put(Location location) {
        put("locations", location, Location::getId);
    }

    /**
     * Helper method to index search data
     *
     * @param type     type name
     * @param data     data
     * @param idMapper mapper to get id from data
     * @param <T>      Data Type
     */
    private <T> void put(String type, T data, Function<T, String> idMapper) {
        doPut("/" + type + "/{id}")
                .path("id", idMapper.apply(data))
                .body(data)
                .asResponse()
                .hasCode(200);
    }

    /**
     * Types:
     * 1. places
     * 2. locations
     *
     * @param type        type to delete before
     * @param updatedDate to delete before updated date
     */
    public void deleteBefore(String type, Date updatedDate) {
        doDelete("/" + type + "/before/{updatedDate}")
                .path("updatedDate", updatedDate.getTime())
                .asResponse()
                .hasCode(200);
    }
}
