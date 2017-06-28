package munch.catalyst.clients;

import com.google.inject.Singleton;
import munch.catalyst.data.Media;
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
public class MediaClient extends RestfulClient {

    @Inject
    public MediaClient(@Named("services.medias.url") String url) {
        super(url);
    }

    public Media put(Media media) {
        return doPut("/places/{placeId}/medias/{mediaId}")
                .path("placeId", media.getPlaceId())
                .path("mediaId", media.getMediaId())
                .body(media)
                .asResponse()
                .asDataObject(Media.class);
    }

    public void deleteBefore(String catalystId, Date updatedDate) {
        doDelete("/places/{placeId}/medias/before/{timestamp}")
                .path("placeId", catalystId)
                .path("timestamp", updatedDate.getTime())
                .asResponse()
                .hasCode(200);
    }
}
