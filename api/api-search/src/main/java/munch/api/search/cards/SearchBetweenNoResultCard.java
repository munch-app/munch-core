package munch.api.search.cards;

/**
 * Created by: Fuxing
 * Date: 8/12/2017
 * Time: 12:36 AM
 * Project: munch-core
 */
public final class SearchBetweenNoResultCard implements SearchCard {
    @Override
    public String getCardId() {
        return "injected_BetweenNoResult_20181011";
    }

    @Override
    public String getUniqueId() {
        return getCardId();
    }
}
