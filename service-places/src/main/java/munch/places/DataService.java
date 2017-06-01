package munch.places;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.places.data.Place;
import munch.places.data.PlaceDatabase;
import munch.places.search.SearchIndex;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 4:13 PM
 * Project: munch-core
 */
@Singleton
public class DataService implements JsonService {
    private static final Logger logger = LoggerFactory.getLogger(DataService.class);

    private final PlaceDatabase database;
    private final SearchIndex index;

    @Inject
    public DataService(PlaceDatabase database, SearchIndex index) {
        this.database = database;
        this.index = index;
    }

    @Override
    public void route() {
        PATH("/places", () -> {
            POST("/get", this::batchGet);

            GET("/:id", this::get);
            PUT("/:id", this::put);
            DELETE("/:id", this::delete);
        });
    }

    /**
     * For this method, check for keys values
     *
     * @param call json call
     * @return empty body = none found
     */
    private List<Place> batchGet(JsonCall call) {
        List<String> keys = call.bodyAsList(String.class);
        return database.get(keys);
    }

    /**
     * @param call json call
     * @return 200 = found, 404 = not found
     */
    private Place get(JsonCall call) {
        String id = call.pathString("id");
        return database.get(id);
    }

    /**
     * @param call json call
     * @return 200 = saved
     */
    private JsonNode put(JsonCall call) throws Exception {
        Place place = call.bodyAsObject(Place.class);
        database.put(place);
        index.put(place);
        return Meta200;
    }

    /**
     * @param call json call
     * @return 200 = deleted
     */
    private JsonNode delete(JsonCall call) throws Exception {
        String id = call.pathString("id");
        index.delete(id);
        database.delete(id);
        return Meta200;
    }
}
