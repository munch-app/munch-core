package munch.api.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import munch.api.search.SearchQuery;
import munch.api.search.SearchRequest;
import munch.data.elastic.ElasticUtils;
import munch.restful.core.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

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

        if (request.isBetween()) {
            sortArray.add(ElasticUtils.Sort.sortDistance(request.getPointsCentroid()));
            return sortArray;
        }

        switch (getSortType(request.getSearchQuery())) {
            case SearchQuery.Sort.TYPE_PRICE_LOWEST:
                sortArray.add(ElasticUtils.Sort.sortField("price.perPax", "asc"));
                break;
            case SearchQuery.Sort.TYPE_PRICE_HIGHEST:
                sortArray.add(ElasticUtils.Sort.sortField("price.perPax", "desc"));
                break;
            case SearchQuery.Sort.TYPE_DISTANCE_NEAREST:
                if (StringUtils.isNotBlank(request.getLatLng())) {
                    logger.warn("Sort by distance by latLng not provided in query.latLng");
                    sortArray.add(ElasticUtils.Sort.sortDistance(request.getLatLng()));
                }
                break;

            default:
            case SearchQuery.Sort.TYPE_MUNCH_RANK:
                sortArray.add(ElasticUtils.Sort.sortField("taste.group", "desc"));
                sortArray.add(ElasticUtils.Sort.sortField("taste.importance", "desc"));
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
}
