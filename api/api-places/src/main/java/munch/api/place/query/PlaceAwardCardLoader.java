package munch.api.place.query;

import munch.user.client.AwardCollectionClient;
import munch.user.data.UserPlaceCollection;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 27/4/2018
 * Time: 7:42 PM
 * Project: munch-core
 */
@Singleton
public final class PlaceAwardCardLoader extends PlaceDataCardLoader<UserPlaceCollection.Item> {

    private final AwardCollectionClient collectionClient;

    @Inject
    public PlaceAwardCardLoader(AwardCollectionClient collectionClient) {
        super("extended_Award_20180904");
        this.collectionClient = collectionClient;
    }

    @Override
    protected List<UserPlaceCollection.Item> query(String placeId) {
        return collectionClient.list(placeId, null, 10);
    }
}
