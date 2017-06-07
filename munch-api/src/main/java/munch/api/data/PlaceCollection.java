package munch.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
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

    // TODO better version of this?, what is this?

    private String name;
    private Filter filter;
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
     * @see Filter
     */
    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
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
    public static class Filter {

        private Place.Filters filters;

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

        public static Filter createTag(String text) {
            Filter filter = new Filter();
            filter.filters = new Place.Filters();
            Place.Filters.Tag tag = new Place.Filters.Tag();
            tag.setPositive(true);
            tag.setText(text);
            filter.filters.setTags(Collections.singleton(tag));
            return filter;
        }
    }
}
