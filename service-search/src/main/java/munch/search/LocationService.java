package munch.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import munch.search.data.Location;
import munch.search.elastic.ElasticClient;
import munch.search.elastic.ElasticIndex;
import munch.search.elastic.LocationBoolQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 6/7/2017
 * Time: 6:36 AM
 * Project: munch-core
 */
@Singleton
public class LocationService implements JsonService {

    private final ElasticIndex index;
    private final ElasticClient client;
    private final LocationBoolQuery boolQuery;
    private final Location.Marshaller locationMarshaller;

    @Inject
    public LocationService(ElasticIndex index, ElasticClient client, LocationBoolQuery boolQuery,
                           Location.Marshaller locationMarshaller) {
        this.index = index;
        this.client = client;
        this.boolQuery = boolQuery;
        this.locationMarshaller = locationMarshaller;
    }

    @Override
    public void route() {
        PATH("/locations", () -> {
            GET("/reverse", this::reverse);
            GET("/suggest", this::suggest);
            PUT("/:id", this::put);

            DELETE("/before/:timestamp", this::deleteBefore);
            DELETE("/:id", this::delete);
        });
    }

    private Location reverse(JsonCall call) throws IOException {
        double lat = call.queryDouble("lat");
        double lng = call.queryDouble("lng");

        JsonNode reverse = boolQuery.reverse(lat, lng);
        JsonNode result = client.postBoolSearch("location", 0, 1, reverse);

        List<Location> locations = toList(result.path("hits").path("hits"));
        if (locations.isEmpty()) return null;
        return locations.stream()
                .sorted((o1, o2) -> Long.compare(o2.getSort(), o1.getSort()))
                .collect(Collectors.toList()).get(0);
    }

    private List<Location> suggest(JsonCall call) throws IOException {
        String text = call.queryString("text").toLowerCase();
        int size = call.queryInt("size");
        if (text.length() < 3) return Collections.emptyList();

        // Location results search
        JsonNode results = client.suggest("location", text, null, size);
        return toList(results);
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

    /**
     * json array parse to list
     *
     * @param results array results
     * @return list of location
     */
    private List<Location> toList(JsonNode results) {
        List<Location> locations = new ArrayList<>();
        for (JsonNode result : results) locations.add(locationMarshaller.deserialize(result));
        return locations;
    }
}
