package munch.api.services.search.cards;

import munch.data.structure.SearchQuery;

/**
 * Created by: Fuxing
 * Date: 8/12/2017
 * Time: 12:36 AM
 * Project: munch-core
 */
public final class SearchNoResultAnywhereCard implements SearchCard {
    private SearchQuery query;

    public SearchNoResultAnywhereCard() {
    }

    public SearchNoResultAnywhereCard(SearchQuery query) {
        this.query = query;
    }

    public SearchQuery getQuery() {
        return query;
    }

    public void setQuery(SearchQuery query) {
        this.query = query;
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
