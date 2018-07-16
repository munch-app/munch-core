package munch.api.search.cards;

/**
 * Created by: Fuxing
 * Date: 20/10/2017
 * Time: 2:17 PM
 * Project: munch-core
 */
public final class SearchNoLocationCard implements SearchCard {

    @Override
    public String getCardId() {
        return "injected_NoLocation_20171020";
    }

    @Override
    public String getUniqueId() {
        return getCardId();
    }
}
