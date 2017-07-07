package munch.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import munch.search.data.Location;
import munch.search.elastic.ElasticDatabase;

/**
 * Created by: Fuxing
 * Date: 6/7/2017
 * Time: 6:36 AM
 * Project: munch-core
 */
@Singleton
public class LocationService implements JsonService {

    private final ElasticDatabase index;

    @Inject
    public LocationService(ElasticDatabase index) {
        this.index = index;
    }

    @Override
    public void route() {
        PATH("/locations", () -> {
            PUT("/:id", this::put);
            // TODO: /reverse
            // TODO: /suggest

            DELETE("/before/:timestamp", this::deleteBefore);
            DELETE("/:id", this::delete);
        });
    }


    /**
     * @param call json call
     * @return 200 = saved
     */
    private JsonNode put(JsonCall call) throws Exception {
        Location location = call.bodyAsObject(Location.class);
        index.put(location);
        return Meta200;
    }

    /**
     * @param call json call
     * @return 200 = deleted
     */
    private JsonNode delete(JsonCall call) throws Exception {
        String id = call.pathString("id");
        index.delete("location", id);
        return Meta200;
    }

    /**
     * @param call json call
     * @return 200 = deleted
     */
    private JsonNode deleteBefore(JsonCall call) throws Exception {
        long updated = call.pathLong("updatedDate");
        index.deleteBefore("location", updated);
        return Meta200;
    }
}
