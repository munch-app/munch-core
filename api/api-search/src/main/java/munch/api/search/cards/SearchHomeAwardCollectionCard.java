package munch.api.search.cards;

import munch.user.data.UserPlaceCollection;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 10/12/18
 * Time: 7:59 PM
 * Project: munch-core
 */
public final class SearchHomeAwardCollectionCard implements SearchCard {

    private final List<UserPlaceCollection> collections;

    public SearchHomeAwardCollectionCard(List<UserPlaceCollection> collections) {
        this.collections = collections;
    }

    public List<UserPlaceCollection> getCollections() {
        return collections;
    }

    @Override
    public String getCardId() {
        return "HomeAwardCollection_2018-12-10";
    }
}
