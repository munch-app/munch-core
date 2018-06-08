package munch.api.services.search.cards;

import munch.data.structure.SearchQuery;

/**
 * Created by: Fuxing
 * Date: 30/1/18
 * Time: 5:01 PM
 * Project: munch-core
 */
public final class SearchQueryReplaceCard implements SearchCard {
    private SearchQuery searchQuery;

    public SearchQueryReplaceCard(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public String getCardId() {
        return "injected_QueryReplace_20180130";
    }

    @Override
    public String getUniqueId() {
        return String.valueOf(System.currentTimeMillis());
    }

    public SearchQuery getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }
}
