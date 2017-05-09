package munch.places.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.annotation.Nullable;
import java.util.Collections;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 9:22 PM
 * Project: munch-core
 */
@Singleton
public class BoolQuery {

    private final ObjectMapper mapper;
    private final Filters emptyFilter;

    @Inject
    public BoolQuery(ObjectMapper mapper) {
        this.mapper = mapper;

        this.emptyFilter = new Filters();
        emptyFilter.setPriceRange(null);
        emptyFilter.setRatingsAbove(null);
        emptyFilter.setHours(Collections.emptySet());
        emptyFilter.setTags(Collections.emptySet());
    }

    /**
     * @param geometry geometry node
     * @param query    query string
     * @param filters  filters
     * @return created bool node
     */
    public JsonNode make(@Nullable JsonNode geometry, @Nullable String query, @Nullable Filters filters) {
        // Fix filter to be more streamlined
        if (filters == null) filters = emptyFilter;
        if (filters.getHours() == null) filters.setHours(Collections.emptySet());
        if (filters.getTags() == null) filters.setTags(Collections.emptySet());

        ObjectNode bool = mapper.createObjectNode();
        bool.set("must", must(query));
        bool.set("must_not", mustNot(filters));
        bool.set("filter", filter(geometry, filters));
        return bool;
    }

    /**
     * Search with text on name
     *
     * @param query query string
     * @return JsonNode must filter
     */
    private JsonNode must(@Nullable String query) {
        ObjectNode must = mapper.createObjectNode();
        // Match all
        if (query == null) {
            return must.set("match_all", mapper.createObjectNode());
        }

        // Match for name
        ObjectNode match = mapper.createObjectNode();
        match.put("name", query);
        return must.set("match", match);
    }

    /**
     * Filter to must not
     *
     * @param filters filters list
     * @return JsonNode must_not filter
     */
    private JsonNode mustNot(Filters filters) {
        ArrayNode notArray = mapper.createArrayNode();

        // Must not filters
        for (Filters.Tag tag : filters.getTags()) {
            if (!tag.isPositive()) {
                notArray.add(filterTerm("tag", tag.getText()));
            }
        }
        return notArray;
    }

    /**
     * @param geometry geometry object
     * @param filters  filters object
     * @return JsonNode bool filter
     */
    private JsonNode filter(@Nullable JsonNode geometry, Filters filters) {
        ArrayNode filterArray = mapper.createArrayNode();

        // Create geometry filter if is not null or missing
        if (geometry != null && !geometry.isNull() && !geometry.isMissingNode()) {
            filterArray.add(filterGeometry(geometry));
        }

        // Filter to positive tags
        for (Filters.Tag tag : filters.getTags()) {
            if (tag.isPositive()) {
                filterArray.add(filterTerm("tag", tag.getText()));
            }
        }

        // TODO price/ratings/hours
        return filterArray;
    }

    /**
     * @param shape node
     * @return JsonNode = { "geo_shape": { "location.geo": {...}}}
     */
    private JsonNode filterGeometry(JsonNode shape) {
        ObjectNode locationGeo = mapper.createObjectNode();
        locationGeo.set("shape", shape);
        locationGeo.put("relation", "within");

        ObjectNode geoShape = mapper.createObjectNode();
        geoShape.set("location.geo", locationGeo);

        ObjectNode filter = mapper.createObjectNode();
        filter.set("geo_shape", geoShape);
        return filter;
    }

    /**
     * @param name name of term
     * @param text text of term
     * @return JsonNode =  { "term" : { "name" : "text" } }
     */
    private JsonNode filterTerm(String name, String text) {
        ObjectNode term = mapper.createObjectNode();
        term.put(name, text);

        ObjectNode filter = mapper.createObjectNode();
        filter.set("term", term);
        return filter;
    }
}
