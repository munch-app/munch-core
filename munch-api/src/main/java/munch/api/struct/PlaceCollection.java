package munch.api.struct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.wololo.geojson.GeoJSON;

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
    private Logic logic;
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
     * @see Logic
     */
    public Logic getLogic() {
        return logic;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
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
    public static class Logic {
        private GeoJSON geometry;
        private Place.Filters filters;

        /**
         * @return geometry of area where search logic is applied
         */
        public GeoJSON getGeometry() {
            return geometry;
        }

        public void setGeometry(GeoJSON geometry) {
            this.geometry = geometry;
        }

        /**
         * The filter can be used to do a search with place client
         * & get back the same results
         *
         * @return filters that is applied for this collection
         */
        public Place.Filters getFilters() {
            return filters;
        }

        public void setFilters(Place.Filters filters) {
            this.filters = filters;
        }
    }
}
