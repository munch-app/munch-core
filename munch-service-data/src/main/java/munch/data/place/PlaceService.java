package munch.data.place;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import munch.struct.Place;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 4:13 PM
 * Project: munch-core
 */
@Singleton
public class PlaceService implements JsonService {

    private final PlaceDatabase database;

    @Inject
    public PlaceService(PlaceDatabase database) {
        this.database = database;
    }

    @Override
    public void route() {
        PATH("/places", () -> {
            PUT("/:id", this::put);
            GET("/:id", this::get);
            DELETE("/:id", this::delete);

            new Batch().route();
        });
    }

    /**
     * @param call json call
     * @return 200 = saved
     */
    private JsonNode put(JsonCall call) {
        //  String id = call.pathString("id");
        Place place = call.bodyAsObject(Place.class);
        database.put(place);
        return Meta200;
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
     * @return 200 = deleted
     */
    private JsonNode delete(JsonCall call) {
        String id = call.pathString("id");
        database.delete(id);
        return Meta200;
    }

    /**
     * Batch request for place services
     */
    private class Batch implements JsonService {

        @Override
        public void route() {
            PATH("/batch", () -> {
                POST("/put", this::put);
                POST("/get", this::get);
                POST("/delete", this::delete);
            });
        }

        /**
         * @param call json call
         * @return 200 = saved
         */
        private JsonNode put(JsonCall call) {
            List<Place> places = call.bodyAsList(Place.class);
            database.put(places);
            return Meta200;
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
         * @return 200 = deleted
         */
        private JsonNode delete(JsonCall call) {
            List<String> keys = call.bodyAsList(String.class);
            database.delete(keys);
            return Meta200;
        }
    }
}
