package munch.api.services.places.loader;

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

    @Inject
    public PlaceInstagramCardLoader(InstagramMediaClient client) {
        super("extended_PartnerInstagramMedia_20180427");
        this.client = client;
    }

    @Override
    protected List<InstagramMedia> query(String placeId) {
        return client.listByPlace(placeId, null, null, 10);
    }
}
