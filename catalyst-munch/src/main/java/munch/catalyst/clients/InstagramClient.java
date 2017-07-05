package munch.catalyst.clients;

import com.google.inject.Singleton;
import munch.catalyst.data.InstagramMedia;
import munch.restful.client.RestfulClient;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 31/5/2017
 * Time: 5:37 AM
 * Project: munch-core
 */
@Singleton
public class InstagramClient extends RestfulClient {

    @Inject
    public InstagramClient(@Named("services.instagram.url") String url) {
        super(url);
    }

    public InstagramMedia put(InstagramMedia media) {
        return doPut("/places/{placeId}/instagram/medias/{mediaId}")
                .path("placeId", media.getPlaceId())
                .path("mediaId", media.getMediaId())
                .body(media)
                .asResponse()
                .asDataObject(InstagramMedia.class);
    }

    public void deleteBefore(String catalystId, Date updatedDate) {
        doDelete("/places/{placeId}/instagram/medias/before/{timestamp}")
                .path("placeId", catalystId)
                .path("timestamp", updatedDate.getTime())
                .asResponse()
                .hasCode(200);
    }
}
