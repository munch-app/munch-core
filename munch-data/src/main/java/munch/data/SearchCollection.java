package munch.data;

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
public final class SearchCollection {
    private String name;
    private SearchQuery query;
    private List<SearchResult> results;

    public SearchCollection() {
    }

    /**
     * Creating a empty Collection with no refilled places
     *
     * @param name  name of collection
     * @param query query for this collection
     */
    public SearchCollection(String name, SearchQuery query) {
        this.name = name;
        this.query = query;
        this.results = Collections.emptyList();
    }

    /**
     * @param name   name of collection
     * @param query  query for this collection
     * @param results pre filled place for this collection
     */
    public SearchCollection(String name, SearchQuery query, List<SearchResult> results) {
        this.name = name;
        this.query = query;
        this.results = results;
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
    public List<SearchResult> getResults() {
        return results;
    }

    public void setResults(List<SearchResult> results) {
        this.results = results;
    }

}
