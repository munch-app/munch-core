package munch.api.search.cards;

import munch.user.data.UserPlaceCollection;

/**
 * Created by: Fuxing
 * Date: 11/12/18
 * Time: 6:52 PM
 * Project: munch-core
 */
public final class SearchCollectionHeaderCard implements SearchCard {

    private final UserPlaceCollection collection;

    public SearchCollectionHeaderCard(UserPlaceCollection collection) {
        this.collection = collection;
    }

    public UserPlaceCollection getCollection() {
        return collection;
    }

    @Override
    public String getCardId() {
        return "CollectionHeader_2018-12-11";
    }
}
