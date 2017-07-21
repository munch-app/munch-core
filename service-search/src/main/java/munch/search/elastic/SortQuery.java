package munch.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.search.data.SearchQuery;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 21/7/2017
 * Time: 5:56 PM
 * Project: munch-core
 */
@Singleton
public class SortQuery {

    private final ObjectMapper mapper;

    @Inject
    public SortQuery(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * @param query SearchQuery for place
     * @return created bool node
     */
    public JsonNode make(SearchQuery query) {
        ArrayNode sortArray = mapper.createArrayNode();
        if (query.getSort() == null) return sortArray;

        SearchQuery.Sort sort = query.getSort();

        // Distance sort
        if (sort.getDistance() != null) {
            sortArray.add(sortDistance(sort.getDistance().getLatLng()));
        }
        return sortArray;
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-request-sort.html
     *
     * @param latLng center
     * @return { "location.latLng" : "lat,lng", "order" : "asc", "unit" : "m", "mode" : "min", "distance_type" : "plane" }
     */
    private JsonNode sortDistance(String latLng) {
        Objects.requireNonNull(latLng);

        ObjectNode geoDistance = mapper.createObjectNode()
                .put("location.latLng", latLng)
                .put("order", "asc")
                .put("unit", "m")
                .put("mode", "min")
                .put("distance_type", "plane");

        ObjectNode filter = mapper.createObjectNode();
        filter.set("_geo_distance", geoDistance);
        return filter;
    }
}
