package munch.api.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.search.SearchRequest;
import munch.api.search.data.SearchQuery;
import munch.restful.core.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 21/7/2017
 * Time: 5:56 PM
 * Project: munch-core
 */
@Singleton
public final class ElasticSortUtils {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSortUtils.class);
    private static final ObjectMapper mapper = JsonUtils.objectMapper;

    /**
     * @param request SearchRequest
     * @return created bool node
     */
    public static JsonNode make(SearchRequest request) {
        ArrayNode sortArray = mapper.createArrayNode();

//        if (request.isBetween()) {
//            sortArray.add(sortDistance(request.getPointsCentroid()));
//            return sortArray;
//        }

        switch (getSortType(request.getSearchQuery())) {
            case SearchQuery.Sort.TYPE_PRICE_LOWEST:
                sortArray.add(sortField("price.perPax", "asc"));
                break;
            case SearchQuery.Sort.TYPE_PRICE_HIGHEST:
                sortArray.add(sortField("price.perPax", "desc"));
                break;
            case SearchQuery.Sort.TYPE_DISTANCE_NEAREST:
                if (StringUtils.isNotBlank(request.getLatLng())) {
                    logger.warn("Sort by distance by latLng not provided in query.latLng");
                    sortArray.add(sortDistance(request.getLatLng()));
                }
                break;
            case SearchQuery.Sort.TYPE_RATING_HIGHEST:
                // Not Implemented yet
                sortArray.add(sortField("review.average", "desc"));
                break;

            default:
            case SearchQuery.Sort.TYPE_MUNCH_RANK:
                sortArray.add(sortField("taste.importance", "desc"));
                break;
        }

        return sortArray;
    }

    /**
     * @return If it is null or blank, default to munchRank
     */
    public static String getSortType(SearchQuery query) {
        return query.getSort() == null ? "" : StringUtils.trimToEmpty(query.getSort().getType());
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-request-sort.html
     *
     * @param latLng center
     * @return { "location.latLng" : "lat,lng", "order" : "asc", "unit" : "m", "mode" : "min", "distance_type" : "plane" }
     */
    public static JsonNode sortDistance(String latLng) {
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
    public static JsonNode sortField(String field, String by) {
        ObjectNode sort = mapper.createObjectNode();
        sort.put(field, by);
        return sort;
    }
}
