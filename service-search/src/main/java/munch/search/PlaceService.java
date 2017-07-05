package munch.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import munch.search.data.Place;
import munch.search.elastic.ElasticDatabase;

import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 6/7/2017
 * Time: 6:36 AM
 * Project: munch-core
 */
@Singleton
public class PlaceService implements JsonService {

    private final ElasticDatabase index;

    @Inject
    public PlaceService(ElasticDatabase index) {
        this.index = index;
    }

    @Override
    public void route() {
        PATH("/places", () -> {
            PUT("/:id", this::put);

            DELETE("/before/:timestamp", this::deleteBefore);
            DELETE("/:id", this::delete);
        });
    }


    /**
     * @param call json call
     * @return 200 = saved
     */
    private JsonNode put(JsonCall call) throws Exception {
        Place place = call.bodyAsObject(Place.class);
        index.put(place);
        return Meta200;
    }

    /**
     * @param call json call
     * @return 200 = deleted
     */
    private JsonNode delete(JsonCall call) throws Exception {
        String id = call.pathString("id");
        index.delete("place", id);
        return Meta200;
    }

    private JsonNode deleteBefore(JsonCall call) {
        Date before = new Date(call.pathLong("timestamp"));
        // TODO Delete Before?
        return Meta200;
    }
}
