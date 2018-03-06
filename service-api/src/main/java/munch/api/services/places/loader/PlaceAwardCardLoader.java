package munch.api.services.places.loader;

import munch.data.extended.PlaceAward;
import munch.data.extended.PlaceAwardClient;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 6/3/18
 * Time: 8:37 PM
 * Project: munch-core
 */
@Singleton
public final class PlaceAwardCardLoader extends PlaceDataCardLoader<PlaceAward, PlaceAwardClient> {

    @Inject
    public PlaceAwardCardLoader(PlaceAwardClient client) {
        super("extended_PlaceAward_20180305", client, 20);
    }
}
