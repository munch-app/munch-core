package munch.api.place.query;

import munch.api.place.PlaceCatalystV2Support;
import munch.corpus.instagram.InstagramMedia;
import munch.corpus.instagram.InstagramMediaClient;

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
public final class PlaceInstagramCardLoader extends PlaceDataCardLoader<InstagramMedia> {

    private final InstagramMediaClient client;
    private final PlaceCatalystV2Support v2Support;

    @Inject
    public PlaceInstagramCardLoader(InstagramMediaClient client, PlaceCatalystV2Support v2Support) {
        super("extended_PartnerInstagramMedia_20180506");
        this.client = client;
        this.v2Support = v2Support;
    }

    @Override
    protected List<InstagramMedia> query(String placeId) {
        return client.listByPlace(v2Support.resolve(placeId), null, 10);
    }
}
