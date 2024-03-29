package munch.api.search.assumption.data;

import munch.api.search.SearchQuery;
import munch.data.place.Place;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 12/5/18
 * Time: 4:41 PM
 * Project: munch-core
 */
public final class AssumptionQueryResult {
    // Might want to send a unique assumption id for tracking

    private SearchQuery searchQuery;
    private List<AssumptionToken> tokens;
    private List<Place> places;
    private long count;

    public SearchQuery getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    public List<AssumptionToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<AssumptionToken> tokens) {
        this.tokens = tokens;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
