package munch.places;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.places.data.PlaceDatabase;
import munch.places.data.Place;
import munch.places.search.SearchIndex;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

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
            GET("/:id", this::get);

            new Batch().route();
        });
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
     * Batch request for place services
     */
    private class Batch implements JsonService {

        @Override
        public void route() {
            PATH("/batch", () -> {
                POST("/get", this::get);
                POST("/put", this::put);
                POST("/delete", this::delete);
            });
        }

        /**
         * For this method, check for keys values
         *
         * @param call json call
         * @return empty body = none found
         */
        private List<Place> get(JsonCall call) {
            List<String> keys = call.bodyAsList(String.class);
            return database.get(keys);
        }

        /**
         * @param call json call
         * @return 200 = saved
         */
        private JsonNode put(JsonCall call) throws Exception {
            List<Place> places = call.bodyAsList(Place.class);
            try {
                database.put(places);
                index.put(places);
            } catch (Exception e) {
                // If any error, delete all from both database and index
                List<String> keys = places.stream().map(Place::getId).collect(Collectors.toList());
                logger.info("Data service put error: {}", keys);
                index.delete(keys);
                database.delete(keys);
            }
            return Meta200;
        }

        /**
         * @param call json call
         * @return 200 = deleted
         */
        private JsonNode delete(JsonCall call) throws Exception {
            List<String> keys = call.bodyAsList(String.class);
            index.delete(keys);
            database.delete(keys);
            return Meta200;
        }
    }
}
