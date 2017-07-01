package munch.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.List;

/**
 * Collection of Place with title and reapply logic
 * <p>
 * Created by: Fuxing
 * Date: 23/4/2017
 * Time: 11:09 AM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceCollection {
    private String name;
    private SearchQuery query;
    private List<Place> places;

    public PlaceCollection() {
    }

    /**
     * Creating a empty Collection with no refilled places
     *
     * @param name  name of collection
     * @param query query for this collection
     */
    public PlaceCollection(String name, SearchQuery query) {
        this.name = name;
        this.query = query;
        this.places = Collections.emptyList();
    }

    /**
     * @param name   name of collection
     * @param query  query for this collection
     * @param places pre filled place for this collection
     */
    public PlaceCollection(String name, SearchQuery query, List<Place> places) {
        this.name = name;
        this.query = query;
        this.places = places;
    }

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
     * If returned query.size is 0 client must start searching query.from 0 also
     *
     * @return SearchQuery = logic to apply to search to get same results
     */
    public SearchQuery getQuery() {
        return query;
    }

    public void setQuery(SearchQuery query) {
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

}
