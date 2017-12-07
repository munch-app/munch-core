package munch.api.services.search.cards;

import munch.data.structure.SearchQuery;

/**
 * Created by: Fuxing
 * Date: 8/12/2017
 * Time: 12:36 AM
 * Project: munch-core
 */
public final class SearchNoResultAnywhereCard implements SearchCard {
    private SearchQuery searchQuery;

    public SearchNoResultAnywhereCard() {
    }

    public SearchNoResultAnywhereCard(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    public SearchQuery getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public String getCardId() {
        return "injected_NoResultAnywhere_20171208";
    }

    @Override
    public String getUniqueId() {
        return getCardId();
    }
}
