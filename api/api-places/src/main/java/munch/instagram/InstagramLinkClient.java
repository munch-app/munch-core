package munch.instagram;

import catalyst.link.PlaceLink;
import catalyst.link.PlaceLinkClient;
import munch.instagram.data.InstagramMedia;
import munch.instagram.data.InstagramMediaClient;
import munch.restful.core.NextNodeList;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 2/9/18
 * Time: 8:13 PM
 * Project: munch-core
 */
@Singleton
public final class InstagramLinkClient {
    private static final String SOURCE = "media.instagram.com";
    private static final String NEXT_KEY = "sourceSortId";

    private final PlaceLinkClient placeLinkClient;
    private final InstagramMediaClient instagramMediaClient;

    @Inject
    public InstagramLinkClient(PlaceLinkClient placeLinkClient, InstagramMediaClient instagramMediaClient) {
        this.placeLinkClient = placeLinkClient;
        this.instagramMediaClient = instagramMediaClient;
    }

    /**
     * @param placeId  id of Place based on V3 Catalyst
     * @param nextSort next sort to start from, null = start from top
     * @param size     per list to query
     * @return List of InstagramMedia, with next sort info
     */
    public NextNodeList<InstagramMedia> list(String placeId, @Nullable String nextSort, int size) {
        NextNodeList<PlaceLink> links = placeLinkClient.listPlaceIdSource(placeId, SOURCE, nextSort, size);

        List<String> mediaIds = links.stream().map(PlaceLink::getId).collect(Collectors.toList());
        Map<String, InstagramMedia> mediaMap = instagramMediaClient.batchGet(mediaIds);

        List<InstagramMedia> mediaList = new ArrayList<>();
        for (PlaceLink link : links) {
            InstagramMedia media = mediaMap.get(link.getId());
            if (media != null) mediaList.add(media);
        }
        return new NextNodeList<>(mediaList, "sort", links.getNextString(NEXT_KEY, null));
    }
}
