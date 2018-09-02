package munch.api.place.query;

import munch.instagram.InstagramLinkClient;
import munch.instagram.data.InstagramMedia;

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

    private final InstagramLinkClient instagramLinkClient;

    @Inject
    public PlaceInstagramCardLoader(InstagramLinkClient instagramLinkClient) {
        super("extended_PartnerInstagramMedia_20180506");
        this.instagramLinkClient = instagramLinkClient;
    }

    @Override
    protected List<InstagramMedia> query(String placeId) {
        return instagramLinkClient.list(placeId, null, 10);
    }
}
