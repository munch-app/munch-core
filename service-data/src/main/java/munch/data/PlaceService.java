package munch.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.hibernate.utils.TransactionProvider;
import munch.data.database.Place;
import munch.data.database.PlaceDatabase;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

import java.util.Date;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 4:13 PM
 * Project: munch-core
 */
@Singleton
public class PlaceService implements JsonService {

    private final TransactionProvider provider;
    private final PlaceDatabase database;

    @Inject
    public PlaceService(TransactionProvider provider, PlaceDatabase database) {
        this.provider = provider;
        this.database = database;
    }

    @Override
    public void route() {
        PATH("/places", () -> {
            POST("/get", this::batchGet);

            GET("/:id", this::get);
            PUT("/:id", this::put);
            DELETE("/before/:timestamp", this::deleteBefore);
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
        return Meta200;
    }

    /**
     * @param call json call
     * @return 200 = deleted
     */
    private JsonNode delete(JsonCall call) throws Exception {
        String id = call.pathString("id");
        database.delete(id);
        return Meta200;
    }

    private JsonNode deleteBefore(JsonCall call) {
        Date before = new Date(call.pathLong("timestamp"));
        provider.with(em -> em.createQuery("DELETE FROM Place WHERE updatedDate < :before")
                .setParameter("before", before)
                .executeUpdate());
        return Meta200;
    }
}
