package munch.api.search.cards;

/**
 * Created by: Fuxing
 * Date: 8/12/2017
 * Time: 12:36 AM
 * Project: munch-core
 */
public final class SearchNoResultCard implements SearchCard {
    @Override
    public String getCardId() {
        return "injected_NoResult_20171208";
    }

    @Override
    public String getUniqueId() {
        return getCardId();
    }
}
