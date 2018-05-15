package munch.api.services.search.cards;

import munch.data.structure.SearchQuery;

/**
 * Created by: Fuxing
 * Date: 8/12/2017
 * Time: 12:36 AM
 * Project: munch-core
 */
public final class SearchNoResultLocationCard implements SearchCard {
    private String locationName;
    private SearchQuery searchQuery;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public SearchQuery getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public String getCardId() {
        return "injected_NoResultLocation_20171208";
    }

    @Override
    public String getUniqueId() {
        return getCardId();
    }
}
