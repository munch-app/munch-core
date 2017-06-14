package munch.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 23/4/2017
 * Time: 11:09 AM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceCollection {
    private String name;
    private Query query;
    private List<Place> places;

    /**
     * Name of collection to display to the use
     * <p>
     * if name is null:
     * is not a collection, it is just basically a list
     * for null name: display as regular list
     *
     * @return name of collection
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return logic to apply to search to get same results
     * @see Query
     */
    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    /**
     * @return list of place; the actual data1
     */
    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    /**
     * Logic for which can be passed to search
     * to get same result as collection
     */
    public static class Query {
        private String query;
        private JsonNode geometry;
        private Place.Filters filters;

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public JsonNode getGeometry() {
            return geometry;
        }

        public void setGeometry(JsonNode geometry) {
            this.geometry = geometry;
        }

        public Place.Filters getFilters() {
            return filters;
        }

        public void setFilters(Place.Filters filters) {
            this.filters = filters;
        }
    }
}
