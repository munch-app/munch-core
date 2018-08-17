package munch.api.place;

import catalyst.link.PlaceLink;
import catalyst.link.PlaceLinkClient;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import munch.restful.core.NextNodeList;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

/**
 * Created by: Fuxing
 * Date: 16/8/18
 * Time: 1:14 AM
 * Project: munch-core
 */
@Singleton
public final class PlaceCatalystV2Support {

    private final LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .maximumSize(35000)
            .expireAfterWrite(2, TimeUnit.DAYS)
            .build(new CacheLoader<>() {
                public String load(String placeId) {
                    return PlaceCatalystV2Support.this.load(placeId);
                }
            });

    private final PlaceLinkClient linkClient;

    @Inject
    public PlaceCatalystV2Support(PlaceLinkClient linkClient) {
        this.linkClient = linkClient;
    }

    public String resolve(String placeId) {
        return cache.getUnchecked(placeId);
    }

    private String load(String placeId) {
        NextNodeList<PlaceLink> links = linkClient.listPlaceIdSource(placeId, "v2.catalyst.munch.space", null, 1);
        for (PlaceLink link : links) {
            if (link.getSource().equals("v2.catalyst.munch.space")) return link.getId();
        }
        return placeId;
    }
}
