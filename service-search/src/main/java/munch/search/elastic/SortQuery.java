package munch.search.elastic;

import catalyst.utils.LatLngUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.data.SearchQuery;
import org.apache.commons.lang3.StringUtils;

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
public final class SortQuery {
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
        if (!validate(query.getSort())) return sortArray;

        switch (query.getSort().getType().toLowerCase()) {
            case SearchQuery.Sort.TYPE_MUNCH_RANK:
                sortArray.add(sortField("munchRank", "desc"));
                break;
            case SearchQuery.Sort.TYPE_PRICE_LOWEST:
                sortArray.add(sortField("price.middle", "asc"));
                break;
            case SearchQuery.Sort.TYPE_PRICE_HIGHEST:
                sortArray.add(sortField("price.middle", "desc"));
                break;
            case SearchQuery.Sort.TYPE_DISTANCE_NEAREST:
                sortArray.add(sortDistance(query.getSort().getLatLng()));
                break;
            case SearchQuery.Sort.TYPE_RATING_HIGHEST:
                // TODO Type Rating in Future
                break;
        }
        return sortArray;
    }

    private boolean validate(SearchQuery.Sort sort) {
        if (sort == null) return false;
        String type = sort.getType();
        if (StringUtils.isBlank(type)) return false;

        if (SearchQuery.Sort.TYPE_DISTANCE_NEAREST.equalsIgnoreCase(type)) {
            try {
                LatLngUtils.validate(sort.getLatLng());
            } catch (LatLngUtils.ParseException e) {
                return false;
            }
        }
        return true;
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

        ObjectNode sort = mapper.createObjectNode();
        sort.set("_geo_distance", geoDistance);
        return sort;
    }

    /**
     * @param field field
     * @param by    direction
     * @return { "field": "by" }
     */
    private JsonNode sortField(String field, String by) {
        ObjectNode sort = mapper.createObjectNode();
        sort.put(field, by);
        return sort;
    }
}
